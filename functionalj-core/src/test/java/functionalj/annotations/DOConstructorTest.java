package functionalj.annotations;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

@SuppressWarnings("javadoc")
public class DOConstructorTest {
    
    @DataObject(
            name = "DONoNoArgsConstructor",
            generateNoArgConstructor = false
        )
    public static interface DONoNoArgsConstructorDef {
        
        public String name();
        
    }
    @Test (expected=InstantiationException.class)
    public void testNoNoArgsConstructor() throws InstantiationException, IllegalAccessException {
        DONoNoArgsConstructor.class.newInstance();
    }
    
    @DataObject(
            name = "DONoAllArgsConstructor",
            generateAllArgConstructor = false
        )
    public static interface DONoAllArgsConstructorDef {
        
        public String name();
        
    }
    @Test(expected=NoSuchMethodException.class)
    public void testNoAllArgsConstructor() throws InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
        DONoAllArgsConstructor.class.getConstructor(String.class).newInstance("Obj");
    }
    
}