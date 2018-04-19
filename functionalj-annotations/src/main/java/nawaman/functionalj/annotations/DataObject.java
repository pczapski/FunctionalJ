package nawaman.functionalj.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataObject {

    public String  name();
    public boolean noArgConstructor()  default true;
    public boolean generateLensClass() default true;
    
}
