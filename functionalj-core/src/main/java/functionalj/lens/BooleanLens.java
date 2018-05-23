package functionalj.lens;

import java.util.function.Predicate;

@FunctionalInterface
public interface BooleanLens<HOST> extends AnyLens<HOST, Boolean> , Predicate<HOST>, BooleanAccess<HOST> {
    
    public static <HOST> BooleanLens<HOST> of(LensSpec<HOST, Boolean> spec) {
        return () -> spec;
    }
    
    public default boolean test(HOST host) {
        return Boolean.TRUE.equals(this.apply(host));
    }
    
    @Override
    default Boolean apply(HOST host) {
        return lensSpec().getRead().apply(host);
    }
    
}
