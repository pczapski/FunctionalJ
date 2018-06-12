package functionalj.compose;

import static functionalj.compose.Functional.compose;
import static functionalj.compose.Functional.curry1;
import static functionalj.compose.Functional.pluck;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Array;
import java.util.function.Function;

import org.junit.Test;

import functionalj.functions.Func2;
import lombok.AllArgsConstructor;

public class FunctionalTest {
    
    @AllArgsConstructor
    public static class Person {
        private String name;
        private int age;
        public String name() {
            return this.name;
        }
        public int getAge() {
            return this.age;
        }
    }
    
    @Test
    public void testPluck() {
        assertEquals("Peter Pan",         pluck("name", new Person("Peter Pan", 50)));
        assertEquals(Integer.valueOf(50), pluck("age",  new Person("Peter Pan", 50)));
        
        Func2<String, Object, Object> pluck = Functional::pluck;
        assertEquals("Peter Pan",         pluck.apply1("name").apply(new Person("Peter Pan", 50)));
        assertEquals(Integer.valueOf(50), pluck.apply1("age").apply(new Person("Peter Pan", 50)));
    }
    
    @Test
    public void testCompose() {
        Function<String, Integer> counts = compose(curry1(String::split, " "), Array::getLength);
        assertEquals("2", counts.apply("One Two").toString());
    }
    
}
