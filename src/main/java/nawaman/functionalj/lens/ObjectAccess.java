package nawaman.functionalj.lens;

import java.util.function.Function;

import nawaman.functionalj.FunctionalJ.Person;

// TODO - Not sure if we still need it.
// NOTE - This might still be useful to have an ability to convert to Lens.
@FunctionalInterface
public interface ObjectAccess<HOST, TYPE> extends AnyEqualableAccess<HOST, TYPE> {
    
    // NOTE: This one thought ... might go.
    public default <T> Function<HOST, T> linkTo(Function<TYPE, T> sub) {
        return host->sub.apply(this.apply(host));
    }
    
}
