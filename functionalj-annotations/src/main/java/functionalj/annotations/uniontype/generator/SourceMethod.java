package functionalj.annotations.uniontype.generator;

import static functionalj.annotations.uniontype.generator.model.Method.Kind.DEFAULT;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.List;

import functionalj.annotations.uniontype.generator.model.Method;
import lombok.Value;
import lombok.val;

@Value
public class SourceMethod implements Lines {
    private final TargetClass targetClass;
    
    @Override
    public List<String> lines() {
        return targetClass
                .spec
                .methods.stream()
                .map    (this::methodToCode)
                .flatMap(List::stream)
                .collect(toList());
    }
    
    private List<String> methodToCode(Method m) {
        val genericsDef = m.generics.isEmpty() ? "" : 
                        "<" + m.generics.stream()
                               .map(g -> g.withBound.replaceAll(" extends Object$", ""))
                               .collect(joining(", ")) + "> ";
        val returnSelf   = m.returnType.toString().equals(targetClass.type.toString());
        val genericCount = targetClass.type.generics.size();
        val returnPrefix = returnSelf ? "Self" + (genericCount == 0 ? "" : genericCount) + ".getAsMe(" : "";
        val returnSuffix = returnSelf ? ")"                            : "";
        if (DEFAULT.equals(m.kind)) {
            if (isThisMethod(m)) {
                return asList(format(
//                        "// m = " + m.toString() + "\n" + 
                        "public %1$s%2$s {\n"
                      + "    return %3$s__spec.%4$s%5$s;\n"
                      + "}", genericsDef, m.definitionForThis(), returnPrefix, m.callForThis(targetClass.type), returnSuffix)
                      .split("\n"));
            } else {
                return asList(format(
//                        "// m = " + m.toString() + "\n" + 
                        "public %1$s%2$s {\n"
                      + "    return %3$s__spec.%4$s%5$s;\n"
                      + "}", genericsDef, m.definition(), returnPrefix, m.call(), returnSuffix)
                      .split("\n"));
            }
        } else {
            return asList(format(
                    "public static %1$s%2$s {\n"
                  + "    return %3$s%4$s.%5$s%6$s;\n"
                  + "}", 
                  genericsDef,
                  m.definition(),
                  returnPrefix,
                  targetClass.spec.sourceType.name,
                  m.call(),
                  returnSuffix)
                  .split("\n"));
        }
    }
    
    private boolean isThisMethod(Method m) {
        return !m.params.isEmpty() && m.params.get(0).type.toString().equals(targetClass.type.toString());
    }
    
}
