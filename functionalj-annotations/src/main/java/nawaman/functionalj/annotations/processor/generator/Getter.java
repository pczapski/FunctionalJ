//  ========================================================================
//  Copyright (c) 2017 Nawapunth Manusitthipol (NawaMan).
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
package nawaman.functionalj.annotations.processor.generator;

import lombok.Value;
import lombok.experimental.Wither;

/**
 * Getter of the input spec.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@Value
@Wither
public class Getter {
    
    private String name;
    private Type type;
    
    /**
     * Create a getter for the name and type.
     * 
     * @param name  the getter name.
     * @param type  the getter type.
     */
    public Getter(String name, Type type) {
        this.name = name;
        this.type = type;
    }
    
}