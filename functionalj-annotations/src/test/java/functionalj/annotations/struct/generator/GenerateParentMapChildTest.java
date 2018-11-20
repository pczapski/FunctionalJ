package functionalj.annotations.struct.generator;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import functionalj.annotations.struct.generator.Getter;
import functionalj.annotations.struct.generator.StructBuilder;
import functionalj.annotations.struct.generator.SourceSpec;
import functionalj.annotations.struct.generator.Type;
import functionalj.annotations.struct.generator.SourceSpec.Configurations;
import functionalj.annotations.struct.generator.model.GenStruct;
import lombok.val;

@SuppressWarnings("javadoc")
public class GenerateParentMapChildTest {
    
    private Configurations configures = new Configurations();
    {
        configures.coupleWithDefinition      = true;
        configures.generateNoArgConstructor  = true;
        configures.generateAllArgConstructor = true;
        configures.generateLensClass         = true;
    }
    
    private String  definitionClassName = "Definitions.ParentDef";
    private String  targetClassName     = "Parent";
    private String  packageName         = "me.test";
    private boolean isClass             = false;
    
    private List<Getter> getters = asList(
            new Getter("children", new Type.TypeBuilder()
                                .simpleName("Map")
                                .generics(asList(
                                        new Type("String", "java.lang"), 
                                        new Type("Child",  "me.test")))
                                .packageName("java.util")
                                .build())
    );
    
    @Test
    public void testParent() {
        val code = generate();
        /* */
        assertEquals(
                "package me.test;\n" + 
                "\n" + 
                "import functionalj.annotations.IPostReConstruct;\n" + 
                "import functionalj.annotations.IStruct;\n" + 
                "import functionalj.annotations.struct.generator.Getter;\n" + 
                "import functionalj.annotations.struct.generator.Type;\n" + 
                "import functionalj.lens.core.LensSpec;\n" + 
                "import functionalj.lens.lenses.MapLens;\n" + 
                "import functionalj.lens.lenses.ObjectLensImpl;\n" + 
                "import functionalj.lens.lenses.StringLens;\n" + 
                "import functionalj.map.ImmutableMap;\n" + 
                "import java.lang.Object;\n" + 
                "import java.util.HashMap;\n" + 
                "import java.util.Map;\n" + 
                "import java.util.function.BiFunction;\n" + 
                "import java.util.function.Function;\n" + 
                "import java.util.function.Supplier;\n" + 
                "import me.test.Child;\n" + 
                "import me.test.Child.ChildLens;\n" + 
                "\n" + 
                "public class Parent implements Definitions.ParentDef,IStruct {\n" + 
                "    \n" + 
                "    public static final ParentLens<Parent> theParent = new ParentLens<>(LensSpec.of(Parent.class));\n" + 
                "    private final Map<String, Child> children;\n" + 
                "    \n" + 
                "    public Parent() {\n" + 
                "        this(null);\n" + 
                "    }\n" + 
                "    public Parent(Map<String, Child> children) {\n" + 
                "        this.children = ImmutableMap.from(children);\n" + 
                "    }\n" + 
                "    \n" + 
                "    public Map<String, Child> children() {\n" + 
                "        return children;\n" + 
                "    }\n" + 
                "    public Parent withChildren(Map<String, Child> children) {\n" + 
                "        return postReConstruct(new Parent(children));\n" + 
                "    }\n" + 
                "    public Parent withChildren(Supplier<Map<String, Child>> children) {\n" + 
                "        return postReConstruct(new Parent(children.get()));\n" + 
                "    }\n" + 
                "    public Parent withChildren(Function<Map<String, Child>, Map<String, Child>> children) {\n" + 
                "        return postReConstruct(new Parent(children.apply(this.children)));\n" + 
                "    }\n" + 
                "    public Parent withChildren(BiFunction<Parent, Map<String, Child>, Map<String, Child>> children) {\n" + 
                "        return postReConstruct(new Parent(children.apply(this, this.children)));\n" + 
                "    }\n" + 
                "    private static Parent postReConstruct(Parent object) {\n" + 
                "        if (object instanceof IPostReConstruct)\n" + 
                "            ((IPostReConstruct)object).postReConstruct();\n" + 
                "        return object;\n" + 
                "    }\n" + 
                "    public static Parent fromMap(Map<String, Object> map) {\n" + 
                "        Map<String, Getter> $schema = getStructSchema();\n" + 
                "        return new Parent(\n" + 
                "                    (Map<String, Child>)IStruct.fromMapValue(map.get(\"children\"), $schema.get(\"children\"))\n" + 
                "                );\n" + 
                "    }\n" + 
                "    public Map<String, Object> toMap() {\n" + 
                "        Map<String, Object> map = new HashMap<>();\n" + 
                "        map.put(\"children\", IStruct.toMapValueObject(children));\n" + 
                "        return map;\n" + 
                "    }\n" + 
                "    public Map<String, Getter> getSchema() {\n" + 
                "        return getStructSchema();\n" + 
                "    }\n" + 
                "    public static Map<String, Getter> getStructSchema() {\n" + 
                "        Map<String, Getter> map = new HashMap<>();\n" + 
                "        map.put(\"children\", new functionalj.annotations.struct.generator.Getter(\"children\", new Type(null, \"Map\", \"java.util\", java.util.Arrays.asList(new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), new Type(null, \"Child\", \"me.test\", java.util.Collections.emptyList()))), functionalj.annotations.DefaultValue.REQUIRED));\n" + 
                "        return map;\n" + 
                "    }\n" + 
                "    public String toString() {\n" + 
                "        return \"Parent[\" + \"children: \" + children() + \"]\";\n" + 
                "    }\n" + 
                "    public int hashCode() {\n" + 
                "        return toString().hashCode();\n" + 
                "    }\n" + 
                "    public boolean equals(Object another) {\n" + 
                "        return (another == this) || ((another != null) && (getClass().equals(another.getClass())) && java.util.Objects.equals(toString(), another.toString()));\n" + 
                "    }\n" + 
                "    \n" + 
                "    public static class ParentLens<HOST> extends ObjectLensImpl<HOST, Parent> {\n" + 
                "        \n" + 
                "        public final MapLens<HOST, String, Child, StringLens<HOST>, ChildLens<HOST>> children = createSubMapLens(Parent::children, Parent::withChildren, StringLens::of, ChildLens::new);\n" + 
                "        \n" + 
                "        public ParentLens(LensSpec<HOST, Parent> spec) {\n" + 
                "            super(spec);\n" + 
                "        }\n" + 
                "        \n" + 
                "    }\n" + 
                "    public static class Builder {\n" + 
                "        \n" + 
                "        public Builder_children children(Map<String, Child> children) {\n" + 
                "            return new Builder_children(children);\n" + 
                "        }\n" + 
                "        \n" + 
                "        public static class Builder_children {\n" + 
                "            \n" + 
                "            private final Map<String, Child> children;\n" + 
                "            \n" + 
                "            private Builder_children(Map<String, Child> children) {\n" + 
                "                this.children = children;\n" + 
                "            }\n" + 
                "            \n" + 
                "            public Map<String, Child> children() {\n" + 
                "                return children;\n" + 
                "            }\n" + 
                "            public Builder_children children(Map<String, Child> children) {\n" + 
                "                return new Builder_children(children);\n" + 
                "            }\n" + 
                "            public Parent build() {\n" + 
                "                return new Parent(children());\n" + 
                "            }\n" + 
                "            \n" + 
                "        }\n" + 
                "        \n" + 
                "    }\n" + 
                "    \n" + 
                "}", code);
        /* */
    }
    
    private String generate() {
        return generate(null);
    }
    
    private String generate(Runnable setting) {
        if (setting != null)
            setting.run();
        
        SourceSpec sourceSpec = new SourceSpec(
                    definitionClassName, // specClassName
                    packageName,         // packageName
                    targetClassName,     // targetClassName
                    packageName,         // targetPackageName
                    isClass,             // isClass
                    null,
                    configures,          // Configurations
                    getters);
        val dataObjSpec = new StructBuilder(sourceSpec).build();
        val generated   = new GenStruct(dataObjSpec).toText();
        return generated;
    }
    
}
