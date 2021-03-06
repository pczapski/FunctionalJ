package functionalj.types.struct.generator;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import functionalj.types.struct.generator.SourceSpec;
import functionalj.types.struct.generator.StructBuilder;
import functionalj.types.struct.generator.Type;
import functionalj.types.struct.generator.model.GenStruct;
import lombok.val;

public class RequiredConstructorTest {

    public static final SourceSpec spec = new functionalj.types.struct.generator.SourceSpec(null,
            "example.functionalj.accesslens", "StructTypeExample", "Person", "example.functionalj.accesslens", null,
            "spec", null,
            new functionalj.types.struct.generator.SourceSpec.Configurations(
                    true, false, true, true, true, true, false, ""),
            java.util.Arrays.asList(
                    new functionalj.types.struct.generator.Getter("firstName",
                            new Type(null, "String", "java.lang", java.util.Collections.emptyList()), false,
                            functionalj.types.DefaultValue.REQUIRED),
                    new functionalj.types.struct.generator.Getter("midName",
                            new Type(null, "String", "java.lang", java.util.Collections.emptyList()), true,
                            functionalj.types.DefaultValue.NULL),
                    new functionalj.types.struct.generator.Getter("lastName",
                            new Type(null, "String", "java.lang", java.util.Collections.emptyList()), false,
                            functionalj.types.DefaultValue.REQUIRED)),
            java.util.Arrays.asList("Person"));
    
    private String generate() {
        val dataObjSpec = new StructBuilder(spec).build();
        val generated   = new GenStruct(spec, dataObjSpec).toText();
        return generated;
    }
    @Test
    public void test() {
        val generated = generate();
        assertEquals(expected, generated);
    }
    
    private String expected =
            "package example.functionalj.accesslens;\n" + 
            "\n" + 
            "import functionalj.lens.core.LensSpec;\n" + 
            "import functionalj.lens.lenses.ObjectLensImpl;\n" + 
            "import functionalj.lens.lenses.StringLens;\n" + 
            "import functionalj.pipeable.Pipeable;\n" + 
            "import functionalj.types.IPostConstruct;\n" + 
            "import functionalj.types.IStruct;\n" + 
            "import functionalj.types.struct.generator.Getter;\n" + 
            "import functionalj.types.struct.generator.SourceSpec;\n" + 
            "import functionalj.types.struct.generator.Type;\n" + 
            "import java.lang.Exception;\n" + 
            "import java.lang.Object;\n" + 
            "import java.util.HashMap;\n" + 
            "import java.util.Map;\n" + 
            "import java.util.function.BiFunction;\n" + 
            "import java.util.function.Function;\n" + 
            "import java.util.function.Supplier;\n" + 
            "\n" + 
            "// example.functionalj.accesslens.StructTypeExample.null\n" + 
            "\n" + 
            "public class Person implements IStruct,Pipeable<Person> {\n" + 
            "    \n" + 
            "    public static final PersonLens<Person> thePerson = new PersonLens<>(LensSpec.of(Person.class));\n" + 
            "    private final String firstName;\n" + 
            "    private final String midName;\n" + 
            "    private final String lastName;\n" + 
            "    public static final SourceSpec spec = new functionalj.types.struct.generator.SourceSpec(null, \"example.functionalj.accesslens\", \"StructTypeExample\", \"Person\", \"example.functionalj.accesslens\", null, \"spec\", null, new functionalj.types.struct.generator.SourceSpec.Configurations(true, false, true, true, true, true, false, \"\"), java.util.Arrays.asList(new functionalj.types.struct.generator.Getter(\"firstName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), false, functionalj.types.DefaultValue.REQUIRED), new functionalj.types.struct.generator.Getter(\"midName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), true, functionalj.types.DefaultValue.NULL), new functionalj.types.struct.generator.Getter(\"lastName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), false, functionalj.types.DefaultValue.REQUIRED)), java.util.Arrays.asList(\"Person\"));\n" + 
            "    \n" + 
            "    public Person(String firstName, String lastName) {\n" + 
            "        this.firstName = $utils.notNull(firstName);\n" + 
            "        this.midName = null;\n" + 
            "        this.lastName = $utils.notNull(lastName);\n" + 
            "        if (this instanceof IPostConstruct) ((IPostConstruct)this).postConstruct();\n" + 
            "    }\n" + 
            "    public Person(String firstName, String midName, String lastName) {\n" + 
            "        this.firstName = $utils.notNull(firstName);\n" + 
            "        this.midName = java.util.Optional.ofNullable(midName).orElseGet(()->null);\n" + 
            "        this.lastName = $utils.notNull(lastName);\n" + 
            "        if (this instanceof IPostConstruct) ((IPostConstruct)this).postConstruct();\n" + 
            "    }\n" + 
            "    \n" + 
            "    public Person __data()  throws Exception  {\n" + 
            "        return this;\n" + 
            "    }\n" + 
            "    public String firstName() {\n" + 
            "        return firstName;\n" + 
            "    }\n" + 
            "    public String midName() {\n" + 
            "        return midName;\n" + 
            "    }\n" + 
            "    public String lastName() {\n" + 
            "        return lastName;\n" + 
            "    }\n" + 
            "    public Person withFirstName(String firstName) {\n" + 
            "        return new Person(firstName, midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withFirstName(Supplier<String> firstName) {\n" + 
            "        return new Person(firstName.get(), midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withFirstName(Function<String, String> firstName) {\n" + 
            "        return new Person(firstName.apply(this.firstName), midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withFirstName(BiFunction<Person, String, String> firstName) {\n" + 
            "        return new Person(firstName.apply(this, this.firstName), midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withMidName(String midName) {\n" + 
            "        return new Person(firstName, midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withMidName(Supplier<String> midName) {\n" + 
            "        return new Person(firstName, midName.get(), lastName);\n" + 
            "    }\n" + 
            "    public Person withMidName(Function<String, String> midName) {\n" + 
            "        return new Person(firstName, midName.apply(this.midName), lastName);\n" + 
            "    }\n" + 
            "    public Person withMidName(BiFunction<Person, String, String> midName) {\n" + 
            "        return new Person(firstName, midName.apply(this, this.midName), lastName);\n" + 
            "    }\n" + 
            "    public Person withLastName(String lastName) {\n" + 
            "        return new Person(firstName, midName, lastName);\n" + 
            "    }\n" + 
            "    public Person withLastName(Supplier<String> lastName) {\n" + 
            "        return new Person(firstName, midName, lastName.get());\n" + 
            "    }\n" + 
            "    public Person withLastName(Function<String, String> lastName) {\n" + 
            "        return new Person(firstName, midName, lastName.apply(this.lastName));\n" + 
            "    }\n" + 
            "    public Person withLastName(BiFunction<Person, String, String> lastName) {\n" + 
            "        return new Person(firstName, midName, lastName.apply(this, this.lastName));\n" + 
            "    }\n" + 
            "    public static Person fromMap(Map<String, Object> map) {\n" + 
            "        Map<String, Getter> $schema = getStructSchema();\n" + 
            "        \n" + 
            "        Person obj = new Person(\n" + 
            "                    (String)IStruct.fromMapValue(map.get(\"firstName\"), $schema.get(\"firstName\")),\n" + 
            "                    (String)IStruct.fromMapValue(map.get(\"midName\"), $schema.get(\"midName\")),\n" + 
            "                    (String)IStruct.fromMapValue(map.get(\"lastName\"), $schema.get(\"lastName\"))\n" + 
            "                );\n" + 
            "        return obj;\n" + 
            "    }\n" + 
            "    public Map<String, Object> toMap() {\n" + 
            "        Map<String, Object> map = new HashMap<>();\n" + 
            "        map.put(\"firstName\", IStruct.toMapValueObject(firstName));\n" + 
            "        map.put(\"midName\", IStruct.toMapValueObject(midName));\n" + 
            "        map.put(\"lastName\", IStruct.toMapValueObject(lastName));\n" + 
            "        return map;\n" + 
            "    }\n" + 
            "    public Map<String, Getter> getSchema() {\n" + 
            "        return getStructSchema();\n" + 
            "    }\n" + 
            "    public static Map<String, Getter> getStructSchema() {\n" + 
            "        Map<String, Getter> map = new HashMap<>();\n" + 
            "        map.put(\"firstName\", new functionalj.types.struct.generator.Getter(\"firstName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), false, functionalj.types.DefaultValue.REQUIRED));\n" + 
            "        map.put(\"midName\", new functionalj.types.struct.generator.Getter(\"midName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), true, functionalj.types.DefaultValue.NULL));\n" + 
            "        map.put(\"lastName\", new functionalj.types.struct.generator.Getter(\"lastName\", new Type(null, \"String\", \"java.lang\", java.util.Collections.emptyList()), false, functionalj.types.DefaultValue.REQUIRED));\n" + 
            "        return map;\n" + 
            "    }\n" + 
            "    public String toString() {\n" + 
            "        return \"Person[\" + \"firstName: \" + firstName() + \", \" + \"midName: \" + midName() + \", \" + \"lastName: \" + lastName() + \"]\";\n" + 
            "    }\n" + 
            "    public int hashCode() {\n" + 
            "        return toString().hashCode();\n" + 
            "    }\n" + 
            "    public boolean equals(Object another) {\n" + 
            "        return (another == this) || ((another != null) && (getClass().equals(another.getClass())) && java.util.Objects.equals(toString(), another.toString()));\n" + 
            "    }\n" + 
            "    \n" + 
            "    public static class PersonLens<HOST> extends ObjectLensImpl<HOST, Person> {\n" + 
            "        \n" + 
            "        public final StringLens<HOST> firstName = createSubLens(Person::firstName, Person::withFirstName, StringLens::of);\n" + 
            "        public final StringLens<HOST> midName = createSubLens(Person::midName, Person::withMidName, StringLens::of);\n" + 
            "        public final StringLens<HOST> lastName = createSubLens(Person::lastName, Person::withLastName, StringLens::of);\n" + 
            "        \n" + 
            "        public PersonLens(LensSpec<HOST, Person> spec) {\n" + 
            "            super(spec);\n" + 
            "        }\n" + 
            "        \n" + 
            "    }\n" + 
            "    public static class Builder {\n" + 
            "        \n" + 
            "        public Builder_firstName firstName(String firstName) {\n" + 
            "            return new Builder_firstName(firstName);\n" + 
            "        }\n" + 
            "        \n" + 
            "        public static class Builder_firstName {\n" + 
            "            \n" + 
            "            private final String firstName;\n" + 
            "            \n" + 
            "            private Builder_firstName(String firstName) {\n" + 
            "                this.firstName = $utils.notNull(firstName);\n" + 
            "            }\n" + 
            "            \n" + 
            "            public String firstName() {\n" + 
            "                return firstName;\n" + 
            "            }\n" + 
            "            public Builder_firstName firstName(String firstName) {\n" + 
            "                return new Builder_firstName(firstName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName midName(String midName) {\n" + 
            "                return new Builder_firstName_midName(this, midName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName_lastName lastName(String lastName) {\n" + 
            "                return midName(null).lastName(lastName);\n" + 
            "            }\n" + 
            "            \n" + 
            "        }\n" + 
            "        public static class Builder_firstName_midName {\n" + 
            "            \n" + 
            "            private final Builder_firstName parent;\n" + 
            "            private final String midName;\n" + 
            "            \n" + 
            "            private Builder_firstName_midName(Builder_firstName parent, String midName) {\n" + 
            "                this.parent = parent;\n" + 
            "                this.midName = java.util.Optional.ofNullable(midName).orElseGet(()->null);\n" + 
            "            }\n" + 
            "            \n" + 
            "            public String firstName() {\n" + 
            "                return parent.firstName();\n" + 
            "            }\n" + 
            "            public String midName() {\n" + 
            "                return midName;\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName firstName(String firstName) {\n" + 
            "                return parent.firstName(firstName).midName(midName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName midName(String midName) {\n" + 
            "                return parent.midName(midName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName_lastName lastName(String lastName) {\n" + 
            "                return new Builder_firstName_midName_lastName(this, lastName);\n" + 
            "            }\n" + 
            "            \n" + 
            "        }\n" + 
            "        public static class Builder_firstName_midName_lastName {\n" + 
            "            \n" + 
            "            private final Builder_firstName_midName parent;\n" + 
            "            private final String lastName;\n" + 
            "            \n" + 
            "            private Builder_firstName_midName_lastName(Builder_firstName_midName parent, String lastName) {\n" + 
            "                this.parent = parent;\n" + 
            "                this.lastName = $utils.notNull(lastName);\n" + 
            "            }\n" + 
            "            \n" + 
            "            public String firstName() {\n" + 
            "                return parent.firstName();\n" + 
            "            }\n" + 
            "            public String midName() {\n" + 
            "                return parent.midName();\n" + 
            "            }\n" + 
            "            public String lastName() {\n" + 
            "                return lastName;\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName_lastName firstName(String firstName) {\n" + 
            "                return parent.firstName(firstName).lastName(lastName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName_lastName midName(String midName) {\n" + 
            "                return parent.midName(midName).lastName(lastName);\n" + 
            "            }\n" + 
            "            public Builder_firstName_midName_lastName lastName(String lastName) {\n" + 
            "                return parent.lastName(lastName);\n" + 
            "            }\n" + 
            "            public Person build() {\n" + 
            "                return new Person(firstName(), midName(), lastName());\n" + 
            "            }\n" + 
            "            \n" + 
            "        }\n" + 
            "        \n" + 
            "    }\n" + 
            "    \n" + 
            "}";
    
}
