package functionalj.types;

import static functionalj.FunctionalJ.it;
import static functionalj.lens.Access.$S;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.Test;

import functionalj.FunctionalJ;
import functionalj.lens.Access;
import lombok.val;

public class StreamableTest {
    
    @Test
    public void testSelectiveMap() {
        assertEquals("[One, --Two, Three, Four, Five]", 
                ImmutableList.of("One", "Two", "Three", "Four", "Five").selectiveMap("Two"::equals, str -> "--" + str).toString());
    }
    
    @Test
    public void testSplit() {
        assertEquals("[[One, Two],[Four, Five],[Three]]", 
                FunctionalListStream.of((Supplier<Stream<String>>)()->Stream.of("One", "Two", "Three", "Four", "Five"))
                .split($S.length().thatEquals(3),
                       $S.length().thatLessThanOrEqualsTo(4))
                .toString());
    }
    @Test
    public void testSplit_noticePredicateCalledMoreThanOncePerItem() {
        val processedStrings = new ArrayList<String>();
        assertEquals("[[One, Two],[Four, Five],[Three]]", 
                FunctionalListStream.of((Supplier<Stream<String>>)()->Stream.of("One", "Two", "Three", "Four", "Five"))
                .split($S.length().thatEquals(3),
                       it -> {
                           processedStrings.add(it);
                           return it.length() <= 4;
                       })
                .toString());
        assertEquals("[Three, Four, Five, Three, Four, Five]", "" + processedStrings);
    }
    
}
