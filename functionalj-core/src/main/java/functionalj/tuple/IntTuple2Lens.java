package functionalj.tuple;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;

import functionalj.lens.core.AccessParameterized;
import functionalj.lens.core.LensSpec;
import functionalj.lens.core.LensSpecParameterized;
import functionalj.lens.core.LensUtils;
import functionalj.lens.core.WriteLens;
import functionalj.lens.lenses.AnyLens;
import functionalj.lens.lenses.IntegerLens;
import functionalj.lens.lenses.ObjectLens;
import lombok.val;


public interface IntTuple2Lens<HOST, T2, T2LENS extends AnyLens<HOST,T2>>
        extends
            ObjectLens<HOST, IntTuple2<T2>>,
            IntTuple2Access<HOST, T2, T2LENS> {

    public static <HOST, T2, T2LENS extends AnyLens<HOST,T2>>
            IntTuple2Lens<HOST, T2, T2LENS> of(
                    Function<HOST,  IntTuple2<T2>>      read,
                    WriteLens<HOST, IntTuple2<T2>>      write,
                    Function<LensSpec<HOST, T2>, T2LENS> valueLensCreator) {
        val spec = new LensSpecParameterized<HOST, IntTuple2<T2>, T2, T2LENS>() {
            @Override public LensSpec<HOST, IntTuple2<T2>> getSpec()          { return LensSpec.of(read, write);        }
            @Override public T2LENS createSubLens(LensSpec<HOST, T2> subSpec) { return valueLensCreator.apply(subSpec); }
        };    
        return ()->spec;
    }

    public LensSpecParameterized<HOST, IntTuple2<T2>, T2, T2LENS> lensSpecParameterized();
    
    @Override
    public default AccessParameterized<HOST, IntTuple2<T2>, T2, T2LENS> accessParameterized() {
        return lensSpecParameterized();
    }

    @Override
    public default LensSpec<HOST, IntTuple2<T2>> lensSpec() {
        return lensSpecParameterized().getSpec();
    }
    
    @Override
    default IntTuple2<T2> applyUnsafe(HOST host) throws Exception {
        return IntTuple2Access.super.apply(host);
    }
    
    public default IntegerLens<HOST> _1() {
        WriteLens<IntTuple2<T2>, Integer> write = (tuple, _1) -> new IntTuple2<T2>(_1, tuple._2);
        Function <IntTuple2<T2>, Integer> read  = IntTuple2::_1;
        return LensUtils.createSubLens(this, read, write, IntegerLens::of);
    }
    
    public default T2LENS T2() {
        val write = (WriteLens<IntTuple2<T2>, T2>)((tuple, T2) -> new IntTuple2<T2>(tuple._1, T2));
        val read  = (Function <IntTuple2<T2>, T2>)IntTuple2::_2;
        return LensUtils.createSubLens(this, read, write, lensSpecParameterized()::createSubLens);
    }
    
    public default Function<HOST, HOST> change1To(int _1value) {
        return host -> {
            val newTuple = new IntTuple2<>(_1value, apply(host)._2);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change2To(T2 T2value) {
        return host -> {
            val newTuple = new IntTuple2<>(apply(host)._1, T2value);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change1By(Supplier<Integer> _1valueSupplier) {
        return host -> {
            val newTuple = new IntTuple2<>(_1valueSupplier.get(), apply(host)._2);
            return apply(host, newTuple);
        };
    }
    public default Function<HOST, HOST> change1By(IntSupplier _1valueSupplier) {
        return host -> {
            val newTuple = new IntTuple2<>(_1valueSupplier.getAsInt(), apply(host)._2);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change2By(Supplier<T2> T2valueSupplier) {
        return host -> {
            val newTuple = new IntTuple2<>(apply(host)._1, T2valueSupplier.get());
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change1By(Function<Integer, Integer> _1valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val new_1    = _1valueTransformer.apply(oldTuple._1);
            val newTuple = new IntTuple2<>(new_1, oldTuple._2);
            return apply(host, newTuple);
        };
    }
    public default Function<HOST, HOST> change1By(IntFunction<Integer> _1valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val new_1    = _1valueTransformer.apply(oldTuple._1);
            val newTuple = new IntTuple2<>(new_1, oldTuple._2);
            return apply(host, newTuple);
        };
    }
    public default Function<HOST, HOST> change1By(ToIntFunction<Integer> _1valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val new_1    = _1valueTransformer.applyAsInt(oldTuple._1);
            val newTuple = new IntTuple2<>(new_1, oldTuple._2);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change2By(Function<T2,T2> T2valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val newT2 = T2valueTransformer.apply(oldTuple._2);
            val newTuple = new IntTuple2<>(oldTuple._1, newT2);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change1By(BiFunction<Integer,T2,Integer> _1valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val new_1    = _1valueTransformer.apply(oldTuple._1, oldTuple._2);
            val newTuple = new IntTuple2<>(new_1, oldTuple._2);
            return apply(host, newTuple);
        };
    }
    
    public default Function<HOST, HOST> change2By(BiFunction<Integer,T2,T2> T2valueTransformer) {
        return host -> {
            val oldTuple = apply(host);
            val newT2    = T2valueTransformer.apply(oldTuple._1, oldTuple._2);
            val newTuple = new IntTuple2<>(oldTuple._1, newT2);
            return apply(host, newTuple);
        };
    }
}