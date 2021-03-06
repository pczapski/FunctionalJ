package functionalj.lens.lenses;

import java.util.function.Function;
import java.util.function.Supplier;

import functionalj.function.Func1;
import functionalj.lens.core.AccessParameterized;
import lombok.val;
import nawaman.nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
@FunctionalInterface
public interface NullableAccess<HOST, TYPE, SUBACCESS extends AnyAccess<HOST, TYPE>> 
            extends
                ObjectAccess<HOST, Nullable<TYPE>>,
                AccessParameterized<HOST, Nullable<TYPE>, TYPE, SUBACCESS> {
    
    public AccessParameterized<HOST, Nullable<TYPE>, TYPE, SUBACCESS> accessWithSub();
    
    @Override
    public default Nullable<TYPE> applyUnsafe(HOST host) throws Exception {
        return accessWithSub().apply(host);
    }
    
    @Override
    public default SUBACCESS createSubAccessFromHost(Function<HOST, TYPE> accessToSub) {
        return accessWithSub().createSubAccessFromHost(accessToSub);
    }
    
    public default SUBACCESS get() {
        return NullableAccess.this.accessWithSub().createSubAccess((Nullable<TYPE> nullable) -> { 
            return nullable.get();
        });
    }
    
    public default <TARGET> 
    NullableAccess<HOST, TARGET, AnyAccess<HOST, TARGET>> map(Function<TYPE, TARGET> mapper) {
        val accessWithSub = new AccessParameterized<HOST, Nullable<TARGET>, TARGET, AnyAccess<HOST,TARGET>>() {
            @Override
            public Nullable<TARGET> applyUnsafe(HOST host) throws Exception {
                Nullable<TYPE> nullable = NullableAccess.this.apply(host);
                if (nullable == null) nullable = Nullable.empty();
                return nullable.map(Func1.from(mapper));
            }
            @Override
            public AnyAccess<HOST, TARGET> createSubAccessFromHost(Function<HOST, TARGET> accessToParameter) {
                return accessToParameter::apply;
            }
        };
        return new NullableAccess<HOST, TARGET, AnyAccess<HOST,TARGET>>() {
            @Override
            public AccessParameterized<HOST, Nullable<TARGET>, TARGET, AnyAccess<HOST, TARGET>> accessWithSub() {
                return accessWithSub;
            }
        };
    }
    
    public default <TARGET> 
    NullableAccess<HOST, TARGET, AnyAccess<HOST, TARGET>> flatMap(Function<TYPE, Nullable<TARGET>> mapper) {
        val accessWithSub = new AccessParameterized<HOST, Nullable<TARGET>, TARGET, AnyAccess<HOST,TARGET>>() {
            @Override
            public Nullable<TARGET> applyUnsafe(HOST host) throws Exception {
                return NullableAccess.this.apply(host).flatMap(mapper);
            }
            @Override
            public AnyAccess<HOST, TARGET> createSubAccessFromHost(Function<HOST, TARGET> accessToParameter) {
                return accessToParameter::apply;
            }
        };
        return new NullableAccess<HOST, TARGET, AnyAccess<HOST,TARGET>>() {
            @Override
            public AccessParameterized<HOST, Nullable<TARGET>, TARGET, AnyAccess<HOST, TARGET>> accessWithSub() {
                return accessWithSub;
            }
        };
    }
    
    public default BooleanAccess<HOST> isPresent() {
        return host -> {
            return NullableAccess.this.apply(host).isPresent();
        };
    }
    public default BooleanAccess<HOST> isNotNull() {
        return host -> {
            return NullableAccess.this.apply(host).isNotNull();
        };
    }
    public default BooleanAccess<HOST> isNull() {
        return host -> {
            return NullableAccess.this.apply(host).isNull();
        };
    }
    
    public default SUBACCESS orElse(TYPE fallbackValue) {
        return NullableAccess.this.accessWithSub().createSubAccess((Nullable<TYPE> nullable) -> { 
            return nullable.orElse(fallbackValue);
        });
    }
    
    public default SUBACCESS orElseGet(Supplier<TYPE> fallbackValueSupplier) {
        return orGet(fallbackValueSupplier);
    }
    public default SUBACCESS orGet(Supplier<TYPE> fallbackValueSupplier) {
        return NullableAccess.this.accessWithSub().createSubAccess((Nullable<TYPE> nullable) -> { 
            return nullable.orElseGet(fallbackValueSupplier);
        });
    }
    
    public default <EXCEPTION extends RuntimeException> SUBACCESS orElseThrow() {
        return NullableAccess.this.accessWithSub().createSubAccess((Nullable<TYPE> nullable) -> { 
            return nullable.orElseThrow();
        });
    }
    
    public default <EXCEPTION extends RuntimeException> SUBACCESS orElseThrow(Supplier<EXCEPTION> exceptionSupplier) {
        return NullableAccess.this.accessWithSub().createSubAccess((Nullable<TYPE> nullable) -> { 
            return nullable.orElseThrow(exceptionSupplier);
        });
    }
    
}
