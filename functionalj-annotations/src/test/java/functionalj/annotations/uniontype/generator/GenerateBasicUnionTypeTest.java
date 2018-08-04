package functionalj.annotations.uniontype.generator;

import functionalj.annotations.UnionType;

public class GenerateBasicUnionTypeTest {
    
    @UnionType
    public static interface Color {
        void White();
        void Black();
        void RGB(int r, int g, int b);
        
        static void validateRGB(int r, int g, int b) {
            if ((r < 0) || (r > 255)) throw new IllegalArgumentException("r: " + r);
            if ((g < 0) || (g > 255)) throw new IllegalArgumentException("g: " + g);
            if ((b < 0) || (b > 255)) throw new IllegalArgumentException("b: " + b);
        }
    }
    
}
