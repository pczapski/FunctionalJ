package functionalj;

import static functionalj.function.Func.cacheFor;
import static functionalj.function.Func.lazy;
import static functionalj.function.Func.withIndex;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.Test;

import lombok.val;

public class ClosureConstantTest {

    
    // Index
    
    @Test
    public void testWithIndex() {
        assertEquals("0: One, 1: Two, 2: Three",
                asList("One", "Two", "Three").stream()
                .map(withIndex((str, idx)->idx + ": " + str))
                .collect(joining(", "))
              );
    }
    @Test
    public void testGrouping() {
        assertEquals("[One, Two], [Three, Four], [Five]",
                asList("One", "Two", "Three", "Four", "Five").stream()
                .collect(Collectors.groupingBy(withIndex((str, idx) -> (idx / 2))))
                .values()
                .stream()
                .map   (each -> each.toString())
                .collect(joining(", "))
              );
    }
    
    
    // Cache
    
    private static Map<String, Integer> counts = new TreeMap<>();
    
    public static String count(String text) {
        counts.compute(text, (t, v)->(v==null)?1:v+1);
        return counts.toString();
    } 
    
    @Test
    public void testCache() {
        Function<String, String> f1 = cacheFor(ClosureConstantTest::count);
        assertEquals("{One=1}", f1.apply("One"));
        assertEquals("{One=1}", f1.apply("One"));
        assertEquals("{One=1, Two=1}", f1.apply("Two"));
        assertEquals("14", f1.andThen(String::length).andThen(String::valueOf).apply("Two"));
        
        Function<String, String> f2 = cacheFor(ClosureConstantTest::count);
        assertEquals("{One=2, Two=1}", f2.apply("One"));
        assertEquals("{One=2, Two=1}", f2.apply("One"));
        assertEquals("{One=2, Two=2}", f2.apply("Two"));
    }
    
    @Test
    public void testLazy() throws InterruptedException {
        val threadCount = 10;
        val callPerThread = 50;
        
        val c = new AtomicInteger();
        val counter = lazy(()->{
            return c.incrementAndGet();
        });
        sleep5();
        // Ensure no running before first call to the counter.
        assertEquals(0, c.intValue());
        
        val latch = new CountDownLatch(threadCount);
        for (int i = 0; i < threadCount; i++) {
            new Thread(()->{
                for (int l = 0; l < callPerThread; l++) {
                    sleep5();
                    // Making the call.
                    counter.get();
                }
                latch.countDown();
            }).start();
        }
        latch.await();
        
        // Ensure that after all so many calls to it, only once does the counter run.
        assertEquals(1, counter.get().intValue());
    }

    private void sleep5() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
}
