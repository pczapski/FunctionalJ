package functionalj.types.choice.generator;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

import functionalj.types.choice.AbstractChoiceClass;
import functionalj.types.choice.ChoiceTypeSwitch;
import functionalj.types.choice.Self;
import functionalj.types.choice.generator.model.SourceSpec;
import functionalj.types.choice.generator.model.Type;
import functionalj.types.choice.generator.model.Method.Kind;
import lombok.Value;
import lombok.val;


@Value
public class TargetClass implements Lines {
    public final SourceSpec spec;
    public final Type       type;
    
    public TargetClass(SourceSpec spec) {
        this.spec = spec;
        this.type = new Type(spec.sourceType.pckg, null, spec.targetName, spec.generics);
    }
    
    // TODO - type can do these.
    
    public String genericParams() {
        return (spec.generics.isEmpty() ? "" : (spec.generics.stream().map(g -> g.name).collect(joining(","))));
    }
    public String generics() {
        return (spec.generics.isEmpty() ? "" : ("<" + genericParams() + ">"));
    }
    public String typeWithGenerics() {
        return type.name + generics();
    }
    public String genericDefParams() {
        return (spec.generics.isEmpty() ? "" : (spec.generics.stream().map(g -> g.withBound).collect(joining(","))));
    }
    public String genericDef() {
        return (spec.generics.isEmpty() ? "" : ("<" + genericDefParams() + ">"));
    }
    public String typeWithGenericDef() {
        return type.name + genericDef();
    }
    
    @Override
    public List<String> lines() {
        val imports     = new TreeSet<String>();
        imports.add("java.util.function.Function");
        imports.add("java.util.function.Consumer");
        imports.add("java.util.function.Predicate");
        imports.add("java.util.function.Supplier");
        imports.add(ChoiceTypeSwitch.class.getCanonicalName());
        imports.add(AbstractChoiceClass.class.getCanonicalName());
        imports.add("functionalj.result.Result");
        imports.add("functionalj.pipeable.Pipeable");
        imports.add("functionalj.lens.core.LensSpec");
        imports.add("functionalj.lens.lenses.*");
        imports.add(spec.sourceType.fullName());
        
        val hasChoiceWuthMoreThanOneParam = spec.choices.stream().anyMatch(c -> c.params.size() >1);
        if (hasChoiceWuthMoreThanOneParam) {
            imports.add("functionalj.types.Absent");
        }
        
        String selfDef = "";
        List<String> specObj = null;
        if (spec.methods.stream().anyMatch(m -> Kind.DEFAULT.equals(m.kind))) {
            // TODO - move this to $utils ?
            imports.add("nawaman.utils.reflection.UProxy");
            imports.add(spec.sourceType.pckg + "." + spec.sourceType.encloseClass + "." + spec.sourceType.name);
            specObj = asList(format("    private final %1$s __spec = UProxy.createDefaultProxy(%2$s.class);", 
                    spec.sourceType.name + spec.sourceType.generics(),
                    spec.sourceType.name));
            
            if (spec.sourceType.generics.isEmpty())
                 selfDef = ", Self";
            else selfDef = ", Self" + spec.sourceType.generics.size() + spec.sourceType.generics();
            
            if (spec.sourceType.generics.isEmpty())
                 imports.add(Self.class.getCanonicalName());
            else imports.add(Self.class.getCanonicalName() + spec.sourceType.generics.size());
        }
        
        spec.choices.stream()
            .map   (c -> c.validationMethod)
            .filter(m -> m != null)
            .findAny()
            .ifPresent(s -> {
                imports.add(spec.sourceType.pckg + "." + spec.sourceType.encloseClass + "." + spec.sourceType.name);
            });
        
        spec.choices.stream()
            .flatMap(c -> c.params.stream())
            .map    (p -> p.type)
            .filter (t -> t.pckg != null)
            .filter (t -> !"java.lang".equals(t.pckg))
            .forEach(t -> imports.add(t.fullName()));
        
        spec.generics.stream()
            .flatMap(g -> g.boundTypes.stream())
            .filter (t -> t.pckg != null)
            .filter (t -> !"java.lang".equals(t.pckg))
            .forEach(t -> imports.add(t.fullName()));
        
        val sourceMethods = new SourceMethod(this).lines().stream()
                .filter(Objects::nonNull)
                .map("    "::concat)
                .collect(toList());;
        
        val subClassConstructors 
                = spec.choices.stream()
                .flatMap(choice -> new SubClassConstructor(this, choice).lines().stream())
                .filter(Objects::nonNull)
                .map("    "::concat)
                .collect(toList());
        
        val subClassDefinitions
                = spec.choices.stream()
                .flatMap(choice -> new SubClassDefinition(this, choice).lines().stream())
                .filter(Objects::nonNull)
                .map("    "::concat)
                .collect(toList());
        
        val targetGeneral
                = new TargetTypeGeneral(this, spec.choices)
                .lines().stream()
                .map("    "::concat)
                .collect(toList());
        
        val targetCheckMethods
                = new SubCheckMethod(this, spec.choices)
                .lines().stream()
                .map("    "::concat)
                .collect(toList());
        
        val switchClasses = range(0, spec.choices.size())
                .mapToObj(index   -> spec.choices.stream().skip(index).collect(toList()))
                .flatMap (choices -> new SwitchClass(this, (choices.size() == spec.choices.size()), choices).lines().stream())
                .filter(Objects::nonNull)
                .map("    "::concat)
                .collect(toList())
                ;
        
        val choiceLens = new ChoiceLensBuilder(spec).build();
        
        val typeName     = typeWithGenerics();
        val pckgName     = spec.sourceType.pckg;
        val importLines  = imports.stream().map(i -> "import " + i + ";").collect(toList());
        val specConstant = (spec.specObjName == null) ? "    " : "    public static final " + SourceSpec.class.getCanonicalName() + " " + spec.specObjName + " = " + spec.toCode() + ";";
        return asList(
                asList(format("package %s;", pckgName)),
                asList(format("")),
                importLines,
                asList(format("")),
                asList("// " + spec.sourceType.fullName()),
                asList(format("")),
                asList(format("@SuppressWarnings({\"javadoc\", \"rawtypes\", \"unchecked\"})")),
                asList(format("public abstract class %1$s extends %6$s<%2$s.%2$sFirstSwitch%3$s> implements Pipeable<%4$s>%5$s {", typeWithGenericDef(), type.name, generics(), typeWithGenerics(), selfDef, AbstractChoiceClass.class.getSimpleName())),
                asList(format("    ")),
                subClassConstructors,
                asList(format("    ")),
                specObj,
                asList(format("    ")),
                choiceLens,
                asList(format("    ")),
                asList(format("    private %s() {}", type.name)),
                asList(format("    public %1$s __data() throws Exception { return this; }",     typeName)),
                asList(format("    public Result<%1$s> toResult() { return Result.valueOf(this); }", typeName)),
                asList(format("    ")),
                subClassDefinitions,
                asList(format("    ")),
                targetGeneral,
                asList(format("    ")),
                sourceMethods,
                asList(format("    ")),
                targetCheckMethods,
                asList(format("    ")),
                switchClasses,
                asList(format("    ")),
                asList(specConstant),
                asList(format("    ")),
                asList(format("}"))
            ).stream()
            .filter (Objects::nonNull)
            .flatMap(List::stream)
            .collect(toList());
    }
}