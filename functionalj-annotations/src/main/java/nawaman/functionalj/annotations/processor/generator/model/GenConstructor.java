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
package nawaman.functionalj.annotations.processor.generator.model;

import static nawaman.functionalj.annotations.processor.generator.ILines.indent;
import static nawaman.functionalj.annotations.processor.generator.ILines.line;
import static nawaman.functionalj.functions.StringFunctions.toStr;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;
import nawaman.functionalj.annotations.processor.generator.IGenerateDefinition;
import nawaman.functionalj.annotations.processor.generator.ILines;
import nawaman.functionalj.annotations.processor.generator.Type;

/**
 * Representation of a generated constructor.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@Value
@Wither
@EqualsAndHashCode(callSuper=false)
public class GenConstructor implements IGenerateDefinition {
    private Accessibility  accessibility;
    private String         name;
    private List<GenParam> params;
    private ILines         body;
    
    @Override
    public Stream<Type> requiredTypes() {
        Set<Type> types = new HashSet<>();
        for (val param : params) {
            val paramType = param.getType();
            if (types.contains(paramType))
                continue;
            
            types.add(paramType);
            param
                .requiredTypes()
                .forEach(types::add);
        }
        return types.stream();
    }
    
    @Override
    public ILines toDefinition() {
        val paramDefs = params.stream().map(GenParam::toTerm).collect(joining(", "));
        val definition = Stream.of(accessibility, name + "(" + paramDefs + ")", "{")
                .map(    toStr())
                .filter( Objects::nonNull)
                .collect(joining(" "));
        return ILines.flatenLines(
                line(definition),
                indent(body),
                line("}"));
    }
    
}