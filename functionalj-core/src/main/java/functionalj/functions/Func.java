package functionalj.functions;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import functionalj.types.Tuple2;
import functionalj.types.Tuple3;
import functionalj.types.Tuple4;

public interface Func {
    
    /**
     * Constructs a Func0 from supplier or lambda.
     * 
     * @param  supplier  the supplier or lambda.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func0.
     **/
    public static <OUTPUT> Func0<OUTPUT> of(Supplier<OUTPUT> supplier) {
        return supplier::get;
    }
    
    /**
     * Constructs a Func1 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT>   the input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func1.
     **/
    public static <INPUT, OUTPUT> Func1<INPUT, OUTPUT> of(Function<INPUT, OUTPUT> function) {
        return function::apply;
    }

    /**
     * Constructs a Func2 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func2.
     **/
    public static <INPUT1, INPUT2,OUTPUT> Func2<INPUT1, INPUT2, OUTPUT> of(BiFunction<INPUT1, INPUT2, OUTPUT> function) {
        return function::apply;
    }
    /**
     * Constructs a Func0 from supplier or lambda.
     * 
     * @param  supplier  the supplier or lambda.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func0.
     **/
    public static <OUTPUT> 
            Func0<OUTPUT> from(Func0<OUTPUT> supplier) {
        return supplier;
    }
    
    /**
     * Constructs a Func1 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT>   the input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func1.
     **/
    public static <INPUT, OUTPUT> 
            Func1<INPUT, OUTPUT> from(Func1<INPUT, OUTPUT> function) {
        return function;
    }

    /**
     * Constructs a Func2 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func2.
     **/
    public static <INPUT1, INPUT2,OUTPUT> 
            Func2<INPUT1, INPUT2, OUTPUT> from(Func2<INPUT1, INPUT2, OUTPUT> function) {
        return function;
    }
    
    /**
     * Constructs a Func3 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <INPUT3>  the third input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func3.
     **/
    public static <INPUT1,INPUT2,INPUT3,OUTPUT> 
            Func3<INPUT1, INPUT2, INPUT3, OUTPUT> from(Func3<INPUT1, INPUT2, INPUT3, OUTPUT> function) {
        return function;
    }
    
    /**
     * Constructs a Func4 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <INPUT3>  the third input data type.
     * @param  <INPUT4>  the forth input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func3.
     **/
    public static
            <INPUT1,INPUT2,INPUT3,INPUT4,OUTPUT> 
            Func4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> from(Func4<INPUT1, INPUT2, INPUT3, INPUT4, OUTPUT> function) {
        return function;
    }
    
    /**
     * Constructs a Func5 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <INPUT3>  the third input data type.
     * @param  <INPUT4>  the forth input data type.
     * @param  <INPUT5>  the fifth input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func3.
     **/
    public static 
            <INPUT1,INPUT2,INPUT3,INPUT4,INPUT5,OUTPUT> 
            Func5<INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, OUTPUT> from(Func5<INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, OUTPUT> function) {
        return function;
    }
    
    /**
     * Constructs a Func2 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <INPUT3>  the third input data type.
     * @param  <INPUT4>  the forth input data type.
     * @param  <INPUT5>  the fifth input data type.
     * @param  <INPUT6>  the sixth input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func3.
     **/
    public static 
            <INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, INPUT6, OUTPUT> 
            Func6<INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, INPUT6, OUTPUT> from(Func6<INPUT1, INPUT2, INPUT3, INPUT4, INPUT5, INPUT6, OUTPUT> function) {
        return function;
    }
    
    
    /**
     * Get the value from the supplier that might be null.
     * 
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param supplier  the suppler.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <O, F extends O> O getOrElse(Supplier<O> supplier, F fallback) {
        if (supplier == null)
            return fallback;
        
        return supplier.get();
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I>       the input type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input     the input value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I, O, F extends O> O applyOrElse(Function<? super I, O> function, I input, F fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input);
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input1    the input 1 value.
     * @param input2    the input 2 value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, O, F extends O> O applyOrElse(
            BiFunction<? super I1, ? super I2, O> function, 
            I1                                    input1, 
            I2                                    input2,
            F                                     fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input1, input2);
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input     the input value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, O, F extends O> O applyOrElse(
            Function<Tuple2<? super I1, ? super I2>, O> function, 
            Tuple2<? super I1, ? super I2>              input,
            F                                           fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input);
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <I3>      the input 3 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input1    the input 1 value.
     * @param input2    the input 2 value.
     * @param input3    the input 3 value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, I3, O, F extends O> O applyOrElse(
            Func3<? super I1, ? super I2, ? super I3, O> function, 
            I1                                           input1,
            I2                                           input2, 
            I3                                           input3, 
            F                                            fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input1, input2, input3);
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <I3>      the input 3 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input     the input value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, I3, O, F extends O> O applyOrElse(
            Function<Tuple3<? super I1, ? super I2, ? super I3>, O> function, 
            Tuple3<? super I1, ? super I2, ? super I3>              input, 
            F                                                       fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input);
    }
    
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <I3>      the input 3 type.
     * @param <I4>      the input 4 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input1    the input 1 value.
     * @param input2    the input 2 value.
     * @param input3    the input 3 value.
     * @param input4    the input 4 value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, I3, I4, O, F extends O> O applyOrElse(
            Func4<? super I1, ? super I2, ? super I3, ? super I4, O> function, 
            I1                                                       input1,
            I2                                                       input2, 
            I3                                                       input3, 
            I4                                                       input4, 
            F                                                        fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input1, input2, input3, input4);
    }
    
    /**
     * Apply the input value to the function that might be null.
     * 
     * @param <I1>      the input 1 type.
     * @param <I2>      the input 2 type.
     * @param <I3>      the input 3 type.
     * @param <I4>      the input 4 type.
     * @param <O>       the output type.
     * @param <F>       the fallback data type
     * @param function  the function.
     * @param input     the input value.
     * @param fallback  the fallback value.
     * @return  the result or the fallback type.
     */
    public static <I1, I2, I3, I4, O, F extends O> O applyOrElse(
            Function<Tuple4<? super I1, ? super I2, ? super I3, ? super I4>, O> function, 
            Tuple4<? super I1, ? super I2, ? super I3, ? super I4>              input, 
            F                                                                   fallback) {
        if (function == null)
            return fallback;
        
        return function.apply(input);
    }
    
    //== All ==
    
    /**
     * Create a high-order function that will take another function that take the given input and return output.
     * NOTE: Not sure if this a traverse.
     * 
     * @param <INPUT>   the input data type.
     * @param <OUTPUT>  the output data type.
     * @param input     the input.
     * @return          the high-order function.
     */
    public static <INPUT,OUTPUT> Func1<Function<INPUT,OUTPUT>, OUTPUT> allApplyTo(INPUT input) {
        return func -> {
            return func.apply(input);
        };
    }
    
    /**
     * Create a high-order function that will take another function that take the given input and return output.
     * NOTE: Not sure if this a traverse.
     * 
     * @param <INPUT>   the input data type.
     * @param input     the input.
     * @return          the high-order function.
     */
    public static <INPUT> Predicate<Function<INPUT, Boolean>> allCheckWith(INPUT input) {
        return func -> {
            return func.apply(input);
        };
    }
    
}