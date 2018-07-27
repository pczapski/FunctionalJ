package functionalj.types.result;

import static functionalj.FunctionalJ.it;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.function.Predicate;

import org.junit.Test;

import lombok.EqualsAndHashCode;
import lombok.Value;

public class ValidTest {
    
    @Value
    @EqualsAndHashCode(callSuper=false)
    public static class Person extends Validatable.With<Person, Person.Checker> {
        private final String name;

        public Person(String name) {
            super(Person.Checker.class, "The value failed to check: %s.");
            this.name = name;
        }
        
        public static class Checker implements Predicate<Person> {
            @Override
            public boolean test(Person t) {
                return t.getName() != null;
            }
        }
    }
    
    @Test
    public void test() {
        Valid<Person> validPerson = Valid.valueOf(new Person("John"));
        assertTrue(validPerson.isValid());
        assertTrue(validPerson.getValue().getName().equals("John"));
        assertEquals("Result:{ Value: ValidTest.Person(name=John) }", validPerson.toString());

        Valid<Person> invalidPerson = Valid.valueOf(new Person(null));
        assertFalse(invalidPerson.isValid());
        assertEquals("Result:{ Exception: functionalj.types.result.ValidationException: The value failed to check: ValidTest.Person(name=null). }", invalidPerson.toString());
        
        Valid<Person> nullPerson = Valid.valueOf(null);
        assertFalse(nullPerson.isValid());
        assertEquals("Result:{ Exception: functionalj.types.result.ValidationException: java.lang.NullPointerException }", nullPerson.toString());
        
        Valid<Person> invalidPerson2 = Invalid.valueOf("No value!");
        assertFalse(invalidPerson2.isValid());
        assertEquals("Result:{ Exception: functionalj.types.result.ValidationException: No value! }", invalidPerson2.toString());
    }
    @Test
    public void testToValidate() {
        Valid<Person> validPerson = Result.of("John").map(Person::new).toValidValue(it());
        assertTrue(validPerson.isValid());
        assertTrue(validPerson.getValue().getName().equals("John"));
        assertEquals("Result:{ Value: ValidTest.Person(name=John) }", validPerson.toString());
        
        Valid<Person> invalidPerson = Result.of("John").filter(str -> false).toValidValue(Person::new);
        assertFalse(invalidPerson.isValid());
        assertEquals("Result:{ Exception: functionalj.types.result.ValidationException: The value failed to check: ValidTest.Person(name=null). }", invalidPerson.toString());
        
    }
    
    private Result<String> someWork(Valid<Person> person) {
        return person.map(Person::getName).otherwise("<NoName>");
    }
    private Result<String> someWork(Person person) {
        return someWork(Valid.valueOf(person));
    }
    
    @Test
    public void testParam() {
        assertEquals("Result:{ Value: John }",     "" + someWork(Valid.valueOf(new Person("John"))));
        assertEquals("Result:{ Value: <NoName> }", "" + someWork(Valid.valueOf(new Person(null))));
        assertEquals("Result:{ Value: <NoName> }", "" + someWork(Valid.valueOf((Person)null)));
        
        assertEquals("Result:{ Value: John }",     "" + someWork(new Person("John")));
        assertEquals("Result:{ Value: <NoName> }", "" + someWork(new Person(null)));
        assertEquals("Result:{ Value: <NoName> }", "" + someWork((Person)null));
        
        assertEquals("Result:{ Value: John }",     "" + someWork(new Person("John").toValidValue()));
        assertEquals("Result:{ Value: <NoName> }", "" + someWork(new Person(null).toValidValue()));
    }
    
    private Result<String> someWorkOnly(Valid<Person> person) {
        return person.map(Person::getName);
    }
    
    @Test
    public void testParamOnly() {
        assertEquals(
                "Result:{ Exception: functionalj.types.result.ValidationException: The value failed to check: ValidTest.Person(name=null). }",
                "" + someWorkOnly(Valid.valueOf(new Person(null))));
        
    }
    
}