//  ========================================================================
//  Copyright (c) 2017-2018 Nawapunth Manusitthipol (NawaMan).
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
package nawaman.functionalj.functions;

/**
 * Function of three parameters.
 * 
 * @param <INPUT1>  the first input data type.
 * @param <INPUT2>  the second input data type.
 * @param <INPUT3>  the third input data type.
 * @param <OUTPUT>  the output data type.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@FunctionalInterface
public interface Func3<INPUT1, INPUT2, INPUT3, OUTPUT> {
    
    /**
     * Constructs a Func2 from function or lambda.
     * 
     * @param  function  the function or lambda.
     * @param  <INPUT1>  the first input data type.
     * @param  <INPUT2>  the second input data type.
     * @param  <INPUT3>  the third input data type.
     * @param  <OUTPUT>  the output data type.
     * @return           the result Func3.
     **/
    public static
            <INPUT1,INPUT2,INPUT3,OUTPUT> 
            Func3<INPUT1, INPUT2, INPUT3, OUTPUT> 
            of(Func3<INPUT1, INPUT2, INPUT3, OUTPUT> function) {
        return function;
    }
    
    
    /**
     * Applies this function to the given input values.
     *
     * @param  input1  the first input.
     * @param  input2  the second input.
     * @param  input3  the third input.
     * @return         the function result.
     */
    public OUTPUT apply(INPUT1 input1, INPUT2 input2, INPUT3 input3);
    
    
    /**
     * Compose this function to the given function.
     * NOTE: Too bad the name 'compose' is already been taken :-(
     * 
     * @param  <FINAL>  the final result value.
     * @param  after    the function to be run after this function.
     * @return          the composed function.
     */
    public default <FINAL> Func3<INPUT1, INPUT2, INPUT3, FINAL> then(Func1<? super OUTPUT, ? extends FINAL> after) {
        return (input1, input2, input3) -> {
            OUTPUT out1 = this.apply(input1, input2, input3);
            FINAL  out2 = after.apply(out1);
            return out2;
        };
    }
    
    /**
     * Create a curry function of the this function.
     * 
     * @return  the curried function.
     */
    public default Func1<INPUT1, Func1<INPUT2, Func1<INPUT3, OUTPUT>>> curry() {
        return i1 -> i2 ->i3 -> this.apply(i1, i2, i3);
    }
    
    //== Partially apply functions ==
    
    @SuppressWarnings("javadoc")
    public default Func2<INPUT2, INPUT3, OUTPUT> apply1(INPUT1 i1) {
        return (i2,i3) -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func2<INPUT1, INPUT3, OUTPUT> apply2(INPUT2 i2) {
        return (i1,i3) -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func2<INPUT1, INPUT2, OUTPUT> apply3(INPUT3 i3) {
        return (i1,i2) -> this.apply(i1, i2, i3);
    }
    
    @SuppressWarnings("javadoc")
    public default Func1<INPUT1, OUTPUT> apply(Absent a1, INPUT2 i2, INPUT3 i3) {
        return i1 -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func1<INPUT2, OUTPUT> apply(INPUT1 i1, Absent a2, INPUT3 i3) {
        return i2 -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func1<INPUT3, OUTPUT> apply(INPUT1 i1, INPUT2 i2, Absent a3) {
        return i3 -> this.apply(i1, i2, i3);
    }
    
    @SuppressWarnings("javadoc")
    public default Func2<INPUT1, INPUT2, OUTPUT> apply(Absent a1, Absent a2, INPUT3 i3) {
        return (i1, i2) -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func2<INPUT1, INPUT3, OUTPUT> apply(Absent a1, INPUT2 i2, Absent a3) {
        return (i1, i3) -> this.apply(i1, i2, i3);
    }
    @SuppressWarnings("javadoc")
    public default Func2<INPUT2, INPUT3, OUTPUT> apply(INPUT1 i1, Absent a2, Absent a3) {
        return (i2, i3) -> this.apply(i1, i2, i3);
    }
    
}