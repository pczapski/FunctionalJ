package functionalj.annotations.uniontype.generator;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

import java.util.Objects;
import java.util.stream.Collectors;

import org.junit.Test;

import functionalj.annotations.uniontype.generator.model.Choice;
import functionalj.annotations.uniontype.generator.model.ChoiceParam;
import functionalj.annotations.uniontype.generator.model.SourceSpec;
import functionalj.annotations.uniontype.generator.model.Type;
import lombok.val;

@SuppressWarnings("javadoc")
public class FirstSubWithOnlyParamTest {
    
    @Test
    public void test() {
        val spec = new SourceSpec(
                "BasicColor",
                new Type("functionalj.annotations.uniontype.generator", "UnionTypeExampleTest", "Union1TypeSpec"),
                asList(
                    new Choice("RGB", "__validateRGB", asList(
                        new ChoiceParam("r", new Type("int"))
                    )),
                    new Choice("White", "RGB(255,255,255)", emptyList())));
        val targetClass = new TargetClass(spec);
        val switchClass = new SwitchClass(targetClass, true, spec.choices);
        val lines  = switchClass.lines().stream().filter(Objects::nonNull).collect(Collectors.joining("\n"));
        assertEquals(
                "public static class BasicColorFirstSwitch {\n" + 
                "    private BasicColor $value;\n" + 
                "    private BasicColorFirstSwitch(BasicColor theValue) { this.$value = theValue; }\n" + 
                "    \n" + 
                "    public <TARGET> BasicColorSwitchWhite<TARGET> rgb(Function<? super RGB, TARGET> theAction) {\n" + 
                "        Function<BasicColor, TARGET> $action = null;\n" + 
                "        Function<BasicColor, TARGET> oldAction = (Function<BasicColor, TARGET>)$action;\n" + 
                "        Function<BasicColor, TARGET> newAction =\n" + 
                "            ($action != null)\n" + 
                "            ? oldAction : \n" + 
                "                ($value instanceof RGB)\n" + 
                "                ? (Function<BasicColor, TARGET>)(d -> theAction.apply((RGB)d))\n" + 
                "                : oldAction;\n" + 
                "        \n" + 
                "        return new BasicColorSwitchWhite<TARGET>($value, newAction);\n" + 
                "    }\n" + 
                "    public <TARGET> BasicColorSwitchWhite<TARGET> rgb(Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(d->theSupplier.get());\n" + 
                "    }\n" + 
                "    \n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgb(Predicate<RGB> check, Function<? super RGB, TARGET> theAction) {\n" + 
                "        Function<BasicColor, TARGET> $action = null;\n" + 
                "        Function<BasicColor, TARGET> oldAction = (Function<BasicColor, TARGET>)$action;\n" + 
                "        Function<BasicColor, TARGET> newAction =\n" + 
                "            ($action != null)\n" + 
                "            ? oldAction : \n" + 
                "                (($value instanceof RGB) && check.test((RGB)$value))\n" + 
                "                ? (Function<BasicColor, TARGET>)(d -> theAction.apply((RGB)d))\n" + 
                "                : oldAction;\n" + 
                "        \n" + 
                "        return new BasicColorSwitchRGBWhite<TARGET>($value, newAction);\n" + 
                "    }\n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgb(Predicate<RGB> check, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(check, d->theSupplier.get());\n" + 
                "    }\n" + 
                "    \n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgbOf(int aR, Function<RGB, TARGET> theAction) {\n" + 
                "        return rgb(rgb -> checkEquals(aR, rgb.r), theAction);\n" + 
                "    }\n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgbOf(int aR, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(rgb -> checkEquals(aR, rgb.r), theSupplier);\n" + 
                "    }\n" + 
                "    \n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgbOf(Predicate<Integer> rCheck, Function<RGB, TARGET> theAction) {\n" + 
                "        return rgb(rgb -> rCheck.test(rgb.r), theAction);\n" + 
                "    }\n" + 
                "    public <TARGET> BasicColorSwitchRGBWhite<TARGET> rgbOf(Predicate<Integer> rCheck, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(rgb -> rCheck.test(rgb.r), theSupplier);\n" + 
                "    }\n" + 
                "}\n" + 
                "public static class BasicColorSwitchRGBWhite<TARGET> extends UnionTypeSwitch<BasicColor, TARGET> {\n" + 
                "    private BasicColorSwitchRGBWhite(BasicColor theValue, Function<BasicColor, TARGET> theAction) { super(theValue, theAction); }\n" + 
                "    \n" + 
                "    public BasicColorSwitchWhite<TARGET> rgb(Function<? super RGB, TARGET> theAction) {\n" + 
                "        Function<BasicColor, TARGET> oldAction = (Function<BasicColor, TARGET>)$action;\n" + 
                "        Function<BasicColor, TARGET> newAction =\n" + 
                "            ($action != null)\n" + 
                "            ? oldAction : \n" + 
                "                ($value instanceof RGB)\n" + 
                "                ? (Function<BasicColor, TARGET>)(d -> theAction.apply((RGB)d))\n" + 
                "                : oldAction;\n" + 
                "        \n" + 
                "        return new BasicColorSwitchWhite<TARGET>($value, newAction);\n" + 
                "    }\n" + 
                "    public BasicColorSwitchWhite<TARGET> rgb(Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(d->theSupplier.get());\n" + 
                "    }\n" + 
                "    \n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgb(Predicate<RGB> check, Function<? super RGB, TARGET> theAction) {\n" + 
                "        Function<BasicColor, TARGET> oldAction = (Function<BasicColor, TARGET>)$action;\n" + 
                "        Function<BasicColor, TARGET> newAction =\n" + 
                "            ($action != null)\n" + 
                "            ? oldAction : \n" + 
                "                (($value instanceof RGB) && check.test((RGB)$value))\n" + 
                "                ? (Function<BasicColor, TARGET>)(d -> theAction.apply((RGB)d))\n" + 
                "                : oldAction;\n" + 
                "        \n" + 
                "        return new BasicColorSwitchRGBWhite<TARGET>($value, newAction);\n" + 
                "    }\n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgb(Predicate<RGB> check, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(check, d->theSupplier.get());\n" + 
                "    }\n" + 
                "    \n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgbOf(int aR, Function<RGB, TARGET> theAction) {\n" + 
                "        return rgb(rgb -> checkEquals(aR, rgb.r), theAction);\n" + 
                "    }\n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgbOf(int aR, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(rgb -> checkEquals(aR, rgb.r), theSupplier);\n" + 
                "    }\n" + 
                "    \n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgbOf(Predicate<Integer> rCheck, Function<RGB, TARGET> theAction) {\n" + 
                "        return rgb(rgb -> rCheck.test(rgb.r), theAction);\n" + 
                "    }\n" + 
                "    public BasicColorSwitchRGBWhite<TARGET> rgbOf(Predicate<Integer> rCheck, Supplier<TARGET> theSupplier) {\n" + 
                "        return rgb(rgb -> rCheck.test(rgb.r), theSupplier);\n" + 
                "    }\n" + 
                "}",
                lines);
    }
    
}
