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
package nawaman.functionalj.kinds;

import nawaman.functionalj.functions.Func1;

/**
 * Functor is data structure with one parameterized type and the flatMap function.
 * 
 * <p>
 * {@code F[a].flatMap(f[a->F[b]]) -> F[b] }
 * </p>
 * 
 * @author NawaMan -- nawa@nawaman.net
 *
 * @param <TYPE>  the functor type.
 * @param <DATA>  the data type
 */
public interface Monad<TYPE, DATA> {
    
    /**
     * Create a Monad of the same type for the given value.
     * 
     * @param  <TARGET>  the target data type.
     * @param  target    the target data value.
     * @return           the newly created monad.
     */
    public <TARGET> Monad<TYPE, TARGET> _of(TARGET target);
    
    /**
     * Map this functor to another functor using the mapper function.
     * 
     * @param <TARGET>  the target data type.
     * @param mapper    the mapper function.
     * @return          another functor.
     */
    public <TARGET> Monad<TYPE, TARGET> flatMap(Func1<DATA, Monad<TYPE, TARGET>> mapper);
    
}