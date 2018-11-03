package functionalj.promise;

import static functionalj.function.Func.carelessly;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import functionalj.function.Func0;
import functionalj.ref.Ref;
import functionalj.ref.RefTo.Default;
import functionalj.result.Result;
import lombok.val;

public class DeferActionCreator {
    
    @Default
    public static final DeferActionCreator instance = new DeferActionCreator();
    
    public static final Ref<DeferActionCreator> current
            = Ref.of(DeferActionCreator.class)
            .defaultTo(DeferActionCreator.instance);
    
    public <D> DeferAction<D> create(
            boolean            interruptOnCancel,
            Func0<D>           supplier,
            Runnable           onStart,
            Consumer<Runnable> runner) {
        val promiseRef = new AtomicReference<Promise<D>>();
        val threadRef  = new AtomicReference<Thread>();
        
        val body = (Runnable)() -> {
            val promise = promiseRef.get();
            if (!promise.isNotDone()) 
                return;
            
            if (interruptOnCancel) {
                threadRef.set(Thread.currentThread());
                promise.eavesdrop(r -> {
                    r.ifCancelled(() -> {
                        val thread = threadRef.get();
                        if (thread != null)
                            thread.interrupt();
                    });
                });
            }
            
            carelessly(onStart);
            
            val action = new PendingAction<D>(promise);
            Result.from(()->{
                try {
                    return supplier.get();
                } finally {
                    if (interruptOnCancel) {
                        threadRef.set(null);
                        // This is to reset the status in case the task was done
                        //   but threadRed is yet to be set to null.
                        Thread.currentThread().isInterrupted();
                    }
                }
            })
            .ifException(action::fail)
            .ifValue    (action::complete);
        };
        val task = StartableAction.runSynchromously.value()
                ? body
                : (Runnable)(() -> runner.accept(body));
        val action = new DeferAction<D>(task, null);
        promiseRef.set(action.getPromise());
        return action;
    }

}
