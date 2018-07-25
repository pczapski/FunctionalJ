package tuple;

import java.util.Map;

@SuppressWarnings("javadoc")
public class Tuple {
    
    public static <T1, T2> Tuple2<T1, T2> of(T1 _1, T2 _2) {
        return new ImmutableTuple2<>(_1, _2);
    }
    
    public static <T1, T2> Tuple2<T1, T2> of(Map.Entry<? extends T1, ? extends T2> entry) {
        if (entry == null)
            return new ImmutableTuple2<>(null, null);
        
        return new ImmutableTuple2<>(entry);
    }
    
    public static <T1, T2, T3> Tuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
        return new ImmutableTuple3<>(_1, _2, _3);
    }
    
    public static <T1, T2, T3, T4> Tuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new ImmutableTuple4<>(_1, _2, _3, _4);
    }
    
    public static <T1, T2, T3, T4, T5> Tuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
        return new ImmutableTuple5<>(_1, _2, _3, _4, _5);
    }
    
    public static <T1, T2, T3, T4, T5, T6> Tuple6<T1, T2, T3, T4, T5, T6> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
        return new ImmutableTuple6<>(_1, _2, _3, _4, _5, _6);
    }
    
}
