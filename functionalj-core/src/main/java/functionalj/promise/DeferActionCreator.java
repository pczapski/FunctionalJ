package functionalj.promise;

import static functionalj.function.Func.carelessly;

import java.util.concurrent.atomic.AtomicReference;

import functionalj.environments.AsyncRunner;
import functionalj.function.Func0;
import functionalj.ref.ComputeBody;
import functionalj.ref.Ref;
import functionalj.result.Result;
import functionalj.supportive.Default;
import lombok.val;

// This class create a defer action with a task.
// Since there are a specific steps for this, we have this one in one place.
// Any customization from Configure or Builder will have to go through this to create DeferAction in the same way.
// This way the customization can be done in a limited way.
public class DeferActionCreator {
    
    @Default
    public static final DeferActionCreator instance = new DeferActionCreator();
    
    public static final Ref<DeferActionCreator> current
                = Ref.of(DeferActionCreator.class)
                        .orTypeDefaultOrGet(DeferActionCreator::new);
    
    public <D> DeferAction<D> create(
            Func0<D>    supplier,
            Runnable    onStart,
            boolean     interruptOnCancel,
            AsyncRunner runner) {
        val promiseRef = new AtomicReference<Promise<D>>();
        val runTask    = new RunTask<D>(interruptOnCancel, supplier, onStart, runner, promiseRef::get);
        val action     = new DeferAction<D>(runTask, null);
        val promise    = action.getPromise();
        System.out.println(promise + ": DeferActionCreator.create");
        promiseRef.set(promise);
        return action;
    }
    
    private static class RunTask<D> implements Runnable {
        
        private final boolean           interruptOnCancel;
        private final Func0<D>          supplier;
        private final Runnable          onStart;
        private final AsyncRunner       runner;
        private final Func0<Promise<D>> promiseRef;
        
        private final AtomicReference<Thread> threadRef  = new AtomicReference<Thread>();
        
        public RunTask(boolean    interruptOnCancel, 
                Func0<D>          supplier, 
                Runnable          onStart, 
                AsyncRunner       runner,
                Func0<Promise<D>> promiseRef) {
            this.interruptOnCancel = interruptOnCancel;
            this.supplier = supplier;
            this.onStart = onStart;
            this.runner = runner;
            this.promiseRef = promiseRef;
        }
        
        @Override
        public void run() {
            AsyncRunner
            .run(runner, new Body())
            .onComplete(result->{
                val promise = promiseRef.get();
                val action = new PendingAction<D>(promise);
                if (result.isValue())
                     action.complete(null);
                else if (result.isCancelled())
                     action.abort(result.exception());
                else action.fail (result.exception());
            });
        }
        
        class Body implements ComputeBody<Void, RuntimeException> {
            public void prepared() {
                val promise = promiseRef.get();
                if (!promise.isNotDone()) 
                    return;
                
                System.out.println(promise + ": prepared ");
                setupInterruptOnCancel(promise);
                
                carelessly(onStart);
            }
            @Override
            public Void compute() throws RuntimeException {
                val promise = promiseRef.get();
                val action = new PendingAction<D>(promise);
                val result = Result.of(this::runSupplier);
                action.completeWith(result);
                return null;
            }
            
            private D runSupplier() {
                try {
                    return supplier.get();
                } finally {
                    doInterruptOnCancel();
                }
            }
            
            private void setupInterruptOnCancel(Promise<D> promise) {
                if (!interruptOnCancel)
                    return;
                
                threadRef.set(Thread.currentThread());
                System.out.println(promise + ": setupInterruptOnCancel");
                promise.eavesdrop(r -> {
                    System.out.println(promise + ": eavesdrop: , r: " + r);
                    r.ifCancelled(() -> {
                        val thread = threadRef.get();
                        System.out.println("ifCancelled: " + promise + ", thread: " + thread);
                        if ((thread != null) && !thread.equals(Thread.currentThread())) {
                            System.out.println("Interrupt!");
                            thread.interrupt();
                        }
                    });
                });
            }
        }
        
        private void doInterruptOnCancel() {
            if (!interruptOnCancel)
                return;
            
            threadRef.set(null);
            // This is to reset the status in case the task was done
            //   but threadRed is yet to be set to null.
            Thread.currentThread().isInterrupted();
        }
    }
    
}
