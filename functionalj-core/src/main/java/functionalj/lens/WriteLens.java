package functionalj.lens;

import java.util.function.BiFunction;

@FunctionalInterface
public interface WriteLens<HOST, DATA> {
    
    public static <HOST, DATA> WriteLens<HOST, DATA> of(BiFunction<HOST, DATA, HOST> biFunction) {
        return (host, newValue)->{
            return biFunction.apply(host, newValue);
        };
    }
    
    public HOST apply(HOST host, DATA newValue);
    
    public default HOST applyTo(HOST host, DATA newValue) {
        return apply(host, newValue);
    }
    
}