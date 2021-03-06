package functionalj.lens.lenses;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import functionalj.tuple.Tuple;
import functionalj.tuple.Tuple2;
import nawaman.nullablej.nullable.Nullable;

@SuppressWarnings("javadoc")
@FunctionalInterface
public interface DoubleAccess<HOST> 
        extends 
            NumberAccess<HOST, Double, DoubleAccess<HOST>>, 
            ToDoubleFunction<HOST>, 
            ConcreteAccess<HOST, Double, DoubleAccess<HOST>> {
    
    @Override
    public default DoubleAccess<HOST> newAccess(Function<HOST, Double> access) {
        return access::apply;
    }
    
    public default double applyAsDouble(HOST host) {
        return apply(host).doubleValue();
    }

    
    public default MathOperators<Double> __mathOperators() {
        return __DoubleMathOperators;
    }
    
    
    public static MathOperators<Double> __DoubleMathOperators = new MathOperators<Double>() {
        
        @Override
        public Double zero() {
            return 0.0;
        }
        @Override
        public Double one() {
            return 1.0;
        }
        @Override
        public Double minusOne() {
            return -1.0;
        }
        @Override
        public Integer toInt(Double number) {
            return toDouble(number).intValue();
        }
        @Override
        public Long toLong(Double number) {
            return toDouble(number).longValue();
        }
        @Override
        public Double toDouble(Double number) {
            return Nullable.of(number).orElse(0.0);
        }
        @Override
        public BigInteger toBigInteger(Double number) {
            return BigInteger.valueOf(toLong(number));
        }
        @Override
        public BigDecimal toBigDecimal(Double number) {
            return new BigDecimal(toDouble(number));
        }
        
        @Override
        public Double add(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return v1 + v2;
        }
        @Override
        public Double subtract(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return v1 - v2;
        }
        @Override
        public Double multiply(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return v1 * v2;
        }
        @Override
        public Double divide(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return v1 / v2;
        }
        @Override
        public Double remainder(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return v1 % v2;
        }

        @Override
        public Tuple2<Double, Double> divideAndRemainder(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return Tuple.of(v1 / v2, v1 % v2);
        }

        @Override
        public Double pow(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return Math.pow(v1, v2);
        }

        @Override
        public Double abs(Double number) {
            double v = (number == null) ? 0 : number.doubleValue();
            return Math.abs(v);
        }
        @Override
        public Double negate(Double number) {
            double v = (number == null) ? 0 : number.doubleValue();
            return -1.0 * v;
        }
        @Override
        public Double signum(Double number) {
            double v = (number == null) ? 0 : number.doubleValue();
            return Math.signum(v);
        }

        @Override
        public Double min(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return Math.min(v1, v2);
        }
        @Override
        public Double max(Double number1, Double number2) {
            double v1 = (number1 == null) ? 0 : number1.doubleValue();
            double v2 = (number2 == null) ? 0 : number2.doubleValue();
            return Math.max(v1, v2);
        }
        
    };
    
}
