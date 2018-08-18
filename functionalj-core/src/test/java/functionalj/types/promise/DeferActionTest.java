package functionalj.types.promise;

import static functionalj.types.promise.DeferAction.run;
import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import lombok.val;

@SuppressWarnings("javadoc")
public class DeferActionTest {
    
    private void assertStrings(String str, Object obj) {
        assertEquals(str, "" + obj);
    }
    
    @Test
    public void testDeferAction() throws InterruptedException {
        val log = new ArrayList<String>();
        val start = System.currentTimeMillis();
        log.add("Start: " + (start - start));
        DeferAction.run(()->{
            Thread.sleep(100);
            return "Hello";
        })
        .subscribe(result -> {
            val end = System.currentTimeMillis();
            log.add("End: " + (10*((end - start) / 10)));
            log.add("Result: " + result);
        });
        
        Thread.sleep(150);
        assertStrings("[Start: 0, End: 100, Result: Result:{ Value: Hello }]", log);
    }
    
    @Test
    public void testDeferAction_abort() throws InterruptedException {
        val deferAction = DeferAction.from(()->{
            Thread.sleep(200);
            return "Hello";
        });
        
        val start  = System.currentTimeMillis();
        val endRef = new AtomicLong();
        val action = deferAction.subscribe(result -> {
            val end = System.currentTimeMillis();
            endRef.set(end - start);
        })
        .start();
        
        Thread.sleep(50);
        action.abort();
        
        assertTrue(endRef.get() < 100);
        assertStrings("Result:{ Exception: functionalj.types.result.ResultCancelledException }", action.getResult());
    }
    
    @Test
    public void testDeferAction_exception() throws InterruptedException {
        val log = new ArrayList<String>();
        val start = System.currentTimeMillis();
        log.add("Start: " + (start - start));
        DeferAction.run(()->{
            Thread.sleep(100);
            throw new IOException("Fail hard!");
        })
        .subscribe(result -> {
            val end = System.currentTimeMillis();
            log.add("End: " + (10*((end - start) / 10)));
            log.add("Result: " + result);
        });
        
        Thread.sleep(150);
        
        assertStrings("[Start: 0, End: 100, Result: Result:{ Exception: java.io.IOException: Fail hard! }]", log);
    }
    
    @Test
    public void testGetResult() {
        val log = new ArrayList<String>();
        val start = System.currentTimeMillis();
        log.add("Start: " + (start - start));
        val result
            = DeferAction.run(()->{
                Thread.sleep(100);
                return "Hello";
            })
            .getResult();
        
        val end = System.currentTimeMillis();
        log.add("End: " + (10*((end - start) / 10)));
        log.add("Result: " + result);
        
        assertStrings("[Start: 0, End: 100, Result: Result:{ Value: Hello }]", log);
    }
    @Test
    public void testGetResult_abort() {
        val log = new ArrayList<String>();
        val start = System.currentTimeMillis();
        log.add("Start: " + (start - start));
        
        val action = run(()->{ sleep(100); return "Hello"; });
        
        try {
            action.getResult(50, TimeUnit.MILLISECONDS);
            fail("Expect an interruption.");
            
        } catch (UncheckedInterruptedException e) {
            val end = System.currentTimeMillis();
            log.add("End: " + (10*((end - start) / 10)));
            log.add("Result: " + action.getCurentResult());
            assertStrings("[Start: 0, End: 50, Result: Result:{ Exception: functionalj.types.result.ResultNotReadyException }]", log);
        }
    }
    @Test
    public void testGetResult_interrupt() {
        val start     = System.currentTimeMillis();
        val threadRef = new AtomicReference<Thread>();
        val action    = run(()->{ threadRef.set(Thread.currentThread()); sleep(200); return "Hello"; });
        
        new Thread(()-> {
            try { sleep(50); } catch (InterruptedException e) { e.printStackTrace(); }
            threadRef.get().interrupt();
        }).start();
        
        action.getResult();
        assertTrue((System.currentTimeMillis() - start) < 100);
        assertStrings("Result:{ Exception: java.lang.InterruptedException: sleep interrupted }", action.getResult());
    }
    
}
