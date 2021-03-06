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
package functionalj.types.struct.generator.model;

import static functionalj.types.struct.generator.ILines.indent;
import static functionalj.types.struct.generator.ILines.line;
import static java.util.stream.Collectors.joining;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

import functionalj.types.struct.Core;
import functionalj.types.struct.generator.IGenerateDefinition;
import functionalj.types.struct.generator.ILines;
import functionalj.types.struct.generator.Type;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.val;
import lombok.experimental.Wither;

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
            
            paramType
                .requiredTypes()
                .forEach(types::add);
            param
                .requiredTypes()
                .forEach(types::add);
            if (paramType.isList())
                types.add(Core.ImmutableList.type());
            if (paramType.isMap())
                types.add(Core.ImmutableMap.type());
            if (paramType.isFuncList())
                types.add(Core.ImmutableList.type());
            if (paramType.isFuncMap())
                types.add(Core.ImmutableMap.type());
            if (paramType.isNullable())
                types.add(Core.Nullable.type());
            if (paramType.isOptional())
                types.add(Core.Optional.type());
        }
        return types.stream();
    }
    
    @Override
    public ILines toDefinition(String currentPackage) {
        val paramDefs = params.stream().map(param -> param.toTerm(currentPackage)).collect(joining(", "));
        val definition = Stream.of(accessibility, name + "(" + paramDefs + ")", "{")
                .map    (utils.toStr())
                .filter (Objects::nonNull)
                .collect(joining(" "));
        return ILines.flatenLines(
                line  (definition),
                indent(body),
                line  ("}"));
    }
    
}