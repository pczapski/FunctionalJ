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
package functionalj.types.struct;

import functionalj.types.struct.generator.Type;

/**
 * This class purpose is to allow decoupling of the annotation projects and the core project.
 * So that the user of the annotation project does not have to include the core in the annotation processing.
 * At the same time provide the safety net check (at least for a developer and as a early alarm)
 *   that there is a mismatch of class names needed by the annotation processor.
 * 
 * The mechanism is to have this class that list all full name of classes (in the core project)
 *   that this project thinks suppose to be like.
 * Then, a test in the core project confirm that assumption.
 * This way, as the whole FunctionJ is built, the assumption check.
 * This does not prevent the problem when different version of the annotation processor and core is used at runtime.
 * But some check is better than nothing.
 * And if that happens (the class name change),
 *   it will be a broken change which should be communicate differently.
 * 
 * This whole mess is from the fact that the interface in the core (those lens classes)
 *   which is support to just define the possible operation and data type now contains the implementation.
 * Until the better solution comes along, Let's do this for now.
 * 
 * @author NawaMan -- nawa@nawaman.net
 */
@SuppressWarnings("javadoc")
public enum Core {
    
    BigDecimalLens("BigDecimalLens"),
    BigIntegerLens("BigIntegerLens"),
    BooleanLens   ("BooleanLens"),
    DoubleLens    ("DoubleLens"),
    FuncListLens  ("FuncListLens"),
    FuncMapLens   ("FuncMapLens"),
    IntegerLens   ("IntegerLens"),
    ListLens      ("ListLens"),
    LongLens      ("LongLens"),
    MapLens       ("MapLens"),
    MayBeLens     ("MayBeLens"),
    NullableLens  ("NullableLens"),
    ObjectLens    ("ObjectLens"),
    ObjectLensImpl("ObjectLensImpl"),
    OptionalLens  ("OptionalLens"),
    StringLens    ("StringLens"),
    
    
    LensSpec ("LensSpec", "functionalj.lens.core"),
    
    FuncList     ("FuncList",      "functionalj.list"),
    FuncMap      ("FuncMap",       "functionalj.map"),
    ImmutableList("ImmutableList", "functionalj.list"),
    ImmutableMap ("ImmutableMap",  "functionalj.map"),
    
    Nullable      ("Nullable",      "nawaman.nullablej.nullable"),
    Optional      ("Optional",      "java.util"),
    
    Pipeable      ("Pipeable",      "functionalj.pipeable"),
    ;
    
    private static final String LENSES_PACKAGE = "functionalj.lens.lenses";
    
    private String simpleName;
    private String packageName;
    private Type   type = null;
    
    private Core(String simpleName) {
        this(simpleName, LENSES_PACKAGE);
    }
    
    private Core(String simpleName, String packageName) {
        this.simpleName  = simpleName;
        this.packageName = packageName;
    }
    
    public String simpleName() {
        return simpleName;
    }
    
    public String packageName() {
        return packageName;
    }
    
    public Type type() {
        if (type == null)
            type = new Type(simpleName, packageName);
        
        return type;
    }
    
}
