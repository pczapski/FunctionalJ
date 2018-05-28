//package functionalj;
//
//import static functionalj.PointFree.chain;
//import static functionalj.PointFree.compose;
//import static functionalj.PointFree.lift;
//import static org.junit.Assert.assertEquals;
//
//import org.junit.Test;
//
//import functionalj.functions.Func1;
//import functionalj.types.MayBe;
//import lombok.val;
//
//public class PointFreeTest {
//    
//    @Test
//    public void testCompose() {
//        val strLength = Func1.of(String::length);
//        val time2     = Func1.of((Integer i) -> i * 2);
//        val plus1     = Func1.of((Integer i) -> i + 1);
//        val strLengthTimes2Plus1 = compose(strLength, time2, plus1);
//        assertEquals("11", strLengthTimes2Plus1.apply("Hello").toString());
//    }
//    
//    @Test
//    public void testLift() {
//        val strLength = lift(String::length);
//        val time2     = lift((Integer i) -> 2*i);
//        val strLengthTimes2 = strLength.andThen(time2);
//        assertEquals("Just(10)", strLengthTimes2.apply(MayBe.of("Hello")).toString());
//        assertEquals("Nothing",  strLengthTimes2.apply(MayBe.of(null)).toString());
//        
//        val composed = compose(
//                        Func1.of(String::length),
//                        Func1.of((Integer i) -> i * 2));
//        assertEquals("Just(10)", lift(composed).apply(MayBe.of("Hello")).toString());
//    }
//    
//    @Test
//    public void testChain() {
//        val strLength = Func1.of((String s)  -> MayBe.of(s.length()));
//        val time2     = Func1.of((Integer i) -> MayBe.of(i * 2));
//        val plus1     = Func1.of((Integer i) -> MayBe.of(i + 1));
//        val strLengthTimes2Plus1 = chain(strLength, time2, plus1);
//        assertEquals(strLengthTimes2Plus1.apply("Hello").toString(), "Just(11)");
//    }
//    
//}
