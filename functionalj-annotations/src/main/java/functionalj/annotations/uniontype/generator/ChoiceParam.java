package functionalj.annotations.uniontype.generator;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class ChoiceParam {
    public final String name;
    public final Type   type;
}