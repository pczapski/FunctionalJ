package functionalj.lens.lenses;

import static functionalj.lens.core.AccessUtils.createNullableAccess;
import static functionalj.lens.core.AccessUtils.createResultAccess;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import functionalj.function.Func1;
import lombok.val;
import nawaman.nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
@FunctionalInterface
public interface AnyAccess<HOST, DATA> 
        extends Func1<HOST, DATA> {
    
    public default AnyAccess<HOST, DATA> newAccess(Function<HOST, DATA> access) {
        return access::apply;
    }
    public default BooleanAccess<HOST> that(Predicate<DATA> checker) {
        return booleanAccess(false, any -> checker.test(any));
    }
    
    public default BooleanAccess<HOST> thatIs(DATA value) {
        return booleanAccess(
                value != null,
                any -> {
                    return any == value;
                });
    }
    public default BooleanAccess<HOST> thatIsNot(DATA value) {
        return booleanAccess(
                value == null,
                any -> {
                    return any != value;
                });
    }
    public default BooleanAccess<HOST> thatEquals(DATA value) {
        return booleanAccess(
                value == null,
                any -> {
                    return Objects.equals(any, value);
                });
    }
    public default BooleanAccess<HOST> thatNotEqualsTo(DATA value) {
        return booleanAccess(
                value == null,
                any -> {
                    return !Objects.equals(any, value);
                });
    }
    public default BooleanAccess<HOST> thatIsNull() {
        return booleanAccess(
                true,
                any -> {
                    return any == null;
                });
    }
    public default BooleanAccess<HOST> thatIsNotNull() {
        return booleanAccess(
                false,
                any -> {
                    return any != null;
                });
    }
    public default IntegerAccess<HOST> getHashCode() {
        return intAccess(
                Integer.MIN_VALUE,
                any -> {
                    return any.hashCode();
                });
    }
    public default StringAccess<HOST> asString() {
        return stringAccess(
                null,
                any -> {
                    return any.toString();
                });
    }
    
    public default IntegerAccess<HOST> intAccess(int defaultValue, Function<DATA, Integer> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    
    public default LongAccess<HOST> longAccess(long defaultValue, Function<DATA, Long> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    
    public default DoubleAccess<HOST> doubleAccess(double defaultValue, Function<DATA, Double> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    public default BigIntegerAccess<HOST> bigIntegerAccess(BigInteger defaultValue, Function<DATA, BigInteger> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    public default BigDecimalAccess<HOST> bigDecimalAccess(BigDecimal defaultValue, Function<DATA, BigDecimal> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    
    public default StringAccess<HOST> stringAccess(String defaultValue, Function<DATA, String> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    
    public default BooleanAccess<HOST> booleanAccess(boolean defaultValue, Function<DATA, Boolean> function) {
        return host -> {
            val value = __internal__.processValue(this, host, defaultValue, function);
            return value;
        };
    }
    
    public default AnyAccess<HOST, DATA> orDefaultTo(DATA fallbackValue) {
        return __internal__.orDefaultTo(this, fallbackValue)::apply;
    }
    public default AnyAccess<HOST, DATA> orDefaultFrom(Supplier<? extends DATA> fallbackValueSupplier) {
        return __internal__.orDefaultFrom(this, fallbackValueSupplier)::apply;
    }
    public default <EXCEPTION extends RuntimeException> AnyAccess<HOST, DATA> orThrow() {
        return __internal__.orThrow(this)::apply;
    }
    public default <EXCEPTION extends RuntimeException> AnyAccess<HOST, DATA> orThrow(Supplier<EXCEPTION> exceptionSupplier) {
        return __internal__.orThrow(this, exceptionSupplier)::apply;
    }
    public default NullableAccess<HOST, DATA, ? extends AnyAccess<HOST, DATA>> toNullable() {
        return __internal__.toNullable(this, f -> (AnyAccess<HOST, DATA>)f::apply);
    }
    
    public static class __internal__ {
        
        public static <HOST, DATA, TARGET> TARGET processValue(AnyAccess<HOST, DATA> access, HOST host, TARGET defaultValue, Function<DATA, TARGET> function) {
            if (host == null)
                return defaultValue;
            
            val value = access.apply(host);
            if (value == null)
                return defaultValue;
            
            val newValue = function.apply(value);
            return newValue;
        }
        
        public static <HOST, DATA> Function<HOST, DATA> orDefaultTo(Function<HOST, DATA> access, DATA fallbackValue) {
            return host -> {
                if (host == null)
                    return fallbackValue;
                
                val value = access.apply(host);
                if (value == null)
                    return fallbackValue;
                
                return value;
            };
        }
        
        public static <HOST, DATA> Function<HOST, DATA> orDefaultFrom(Function<? super HOST, DATA> access, Supplier<? extends DATA> fallbackValueSupplier) {
            return host -> {
                if (host == null)
                    return fallbackValueSupplier.get();
                
                val value = access.apply(host);
                if (value == null)
                    return fallbackValueSupplier.get();
                
                return value;
            };
        }
        public static <HOST, DATA> Function<HOST, DATA> orThrow(Function<HOST, DATA> access) {
            return host -> {
                if (host == null)
                    throw new NullPointerException();
                
                val value = access.apply(host);
                if (value == null)
                    throw new NullPointerException();
                
                return value;
            };
        }
        public static <HOST, DATA, EXCEPTION extends RuntimeException> 
                Function<HOST, DATA> orThrow(Function<HOST, DATA> access, Supplier<EXCEPTION> exceptionSupplier) {
            return host -> {
                if (host == null)
                    throw exceptionSupplier.get();
                
                val value = access.apply(host);
                if (value == null)
                    throw exceptionSupplier.get();
                
                return value;
            };
        }
        public static <HOST, DATA, ACCESS extends AnyAccess<HOST, DATA>> 
                NullableAccess<HOST, DATA, ACCESS> toNullable(
                        Function<HOST, DATA>                   access, 
                        Function<Function<HOST, DATA>, ACCESS> createSubLens) {
            return createNullableAccess(
                    host -> {
                        val value = access.apply(host);
                        return Nullable.of(value);
                    },
                    createSubLens);
        }
        public static <HOST, DATA, ACCESS extends AnyAccess<HOST, DATA>> 
                ResultAccess<HOST, DATA, ACCESS> toResult(
                        Function<HOST, DATA>                   access, 
                        Function<Function<HOST, DATA>, ACCESS> createSubLens) {
            return createResultAccess(
                    Func1.from(access)::applySafely,
                    createSubLens);
        }
    }
    
}
