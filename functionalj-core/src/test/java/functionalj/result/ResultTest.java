package functionalj.result;

import static functionalj.functions.Func.F;
import static functionalj.result.Result.Do;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import functionalj.result.Result;
import functionalj.validator.Validator;
import lombok.val;

public class ResultTest {
    
    private static final Result<String> result = Result.of("Test");
    
    private void assertStrings(String str, Object obj) {
        assertEquals(str, "" + obj);
    }
    
    @Test
    public void testMap() {
        assertStrings("Result:{ Value: Test }", result);
        assertStrings("Result:{ Value: 4 }",    result.map(str -> str.length()));
    }
    
    @Test
    public void testResultFom() {
        assertEquals("Result:{ Value: VALUE }", "" + Result.from(()->"VALUE"));
        assertEquals("Result:{ Exception: java.io.IOException }",
                "" + Result.from(F(()->{ throw new IOException(); })));
    }
    @Test
    public void testResult_value() {
        val result = Result.of("VALUE");
        assertEquals("Result:{ Value: VALUE }", "" + result);
        assertTrue (result.isValue());
        assertFalse(result.isException());
        assertTrue (result.isPresent());
        assertTrue (result.isNotNull());
        assertFalse(result.isNull());
    }
    
    @Test
    public void testResult_null() {
        val result = Result.of(null);
        assertEquals("Result:{ Value: null }", "" + result);
        assertTrue (result.isValue());
        assertFalse(result.isException());
        assertFalse(result.isPresent());
        assertFalse(result.isNotNull());
        assertTrue (result.isNull());
    }
    
    @Test
    public void testResult_exception() {
        val result = Result.of((String)null).ensureNotNull();
        assertEquals("Result:{ Exception: java.lang.NullPointerException }", "" + result);
        assertFalse(result.isValue());
        assertTrue (result.isException());
        assertFalse(result.isPresent());
        assertFalse(result.isNotNull());
        assertFalse(result.isNull());
    }
    
    @Test
    public void testResult_map() {
        val result = Result.of("VALUE").map(str -> str.length());
        assertEquals("Result:{ Value: 5 }", "" + result);
        assertTrue (result.isValue());
        assertFalse(result.isException());
        assertTrue (result.isPresent());
        assertTrue (result.isNotNull());
        assertFalse(result.isNull());
    }
    
    @Test
    public void testResult_failableMap() {
        val result = Result.of("VALUE").map(str -> new UnsupportedOperationException("Not support."));
        assertEquals("Result:{ Value: java.lang.UnsupportedOperationException: Not support. }", "" + result);
        assertTrue (result.isValue());
        assertFalse(result.isException());
        assertTrue (result.isPresent());
        assertTrue (result.isNotNull());
        assertFalse(result.isNull());
    }
    
    @Test
    public void testResult_map_null() {
        val result = Result.of("VALUE").map(str -> (String)null).map(String::length);
        assertEquals("Result:{ Value: null }", "" + result);
    }
    @Test
    public void testResult_validate() {
        val validator1 = Validator.of((String s) -> s.toUpperCase().equals(s), "Not upper case");
        val validator2 = Validator.of((String s) -> s.matches("^.*[A-Z].*$"),  "No upper case");
        val validator3 = Validator.of((String s) -> !s.isEmpty(),              "Empty");
        assertEquals("Result:{ Value: (VALUE,[]) }", "" + Result.of("VALUE").validate(validator1, validator2));
        assertEquals("Result:{ Value: (value,["
                +   "functionalj.types.result.ValidationException: Not upper case, "
                +   "functionalj.types.result.ValidationException: No upper case"
                + "]) }",
                "" + Result.of("value").validate(validator1, validator2, validator3));
        assertEquals("Result:{ Value: (,["
                +   "functionalj.types.result.ValidationException: No upper case, "
                +   "functionalj.types.result.ValidationException: Empty]) }",
                "" + Result.of("").validate(validator1, validator2, validator3));
        
        assertEquals("Result:{ Value: (null,["
                +   "functionalj.types.result.ValidationException: java.lang.NullPointerException, "
                +   "functionalj.types.result.ValidationException: java.lang.NullPointerException, "
                +   "functionalj.types.result.ValidationException: java.lang.NullPointerException"
                + "]) }",
                "" + Result.of((String)null).validate(validator1, validator2, validator3));
    }
    
    @Test
    public void testResultOf() {
        assertStrings("Result:{ Value: One }", Result.of("One"));
        
        assertStrings("Result:{ Value: One,Two }",
                Result.of("One", "Two", (a, b)-> a + "," + b));
        
        assertStrings("Result:{ Value: One,Two,Three }",
                Result.of("One", "Two", "Three", (a, b, c)-> a + "," + b + "," + c));
    }
    
    @Test
    public void testResultResult() {
        assertStrings(
                "Result:{ Value: One }",
                Result.ofResult(Result.of("One")));
        assertStrings("Result:{ Value: One,Two }",
                Result.ofResults(
                        Result.of("One"),
                        Result.of("Two"),
                        (a, b)-> a + "," + b));
        
        assertStrings("Result:{ Value: One,Two,Three }",
                Result.ofResults(
                        Result.of("One"),
                        Result.of("Two"),
                        Result.of("Three"),
                        (a, b, c)-> a + "," + b + "," + c));
    }
    
    @Test
    public void testResultResult_withException() {
        assertStrings("Result:{ Exception: functionalj.functions.FunctionInvocationException: Test fail }",
                Result.ofResults(
                        Result.of("One"),
                        Result.ofException("Test fail"),
                        Result.of("Three"),
                        (a, b, c)-> a + "," + b + "," + c));
    }
    
    @Test
    public void testResultPeek() {
        assertStrings("Result:{ Value: 3 }", 
                Result.of("One")
                .pipe(
                    r -> r.map(String::length),
                    String::valueOf));
    }
    
    @Test
    public void testResultDo() {
        assertStrings(
                "Result:{ Value: One }",
                Result.ofResult(Result.of("One")));
        
        assertStrings("Result:{ Value: One,Two }",
                Do(
                  ()->"One",
                  ()->"Two",
                  (a, b)-> a + "," + b));
    }
    
    @Test
    public void testResultDo_withException() {
        assertStrings(
                "Result:{ Exception: java.lang.RuntimeException: Test exception }",
                Result.ofResult(Result.ofException(new RuntimeException("Test exception"))));
        
        assertStrings("Result:{ Exception: java.lang.RuntimeException: Test exception }",
                Do(
                  ()->"One",
                  ()->{ throw new RuntimeException("Test exception"); },
                  (a, b)-> a + "," + b));
    }
    
}