package functionalj.tuple;

import java.util.Map;

import lombok.val;

@SuppressWarnings("javadoc")
public class ImmutableTuple {
    
    private ImmutableTuple() {}
    
    
    public static <T1, T2> ImmutableTuple2<T1, T2> of(T1 _1, T2 _2) {
        return new ImmutableTuple2<>(_1, _2);
    }
    
    public static <T1, T2> ImmutableTuple2<T1, T2> of(Map.Entry<? extends T1, ? extends T2> entry) {
        if (entry == null)
            return new ImmutableTuple2<>(null, null);
        
        return new ImmutableTuple2<>(entry);
    }
    
    public static <T1, T2, T3> ImmutableTuple3<T1, T2, T3> of(T1 _1, T2 _2, T3 _3) {
        return new ImmutableTuple3<>(_1, _2, _3);
    }
    
    public static <T1, T2, T3, T4> ImmutableTuple4<T1, T2, T3, T4> of(T1 _1, T2 _2, T3 _3, T4 _4) {
        return new ImmutableTuple4<>(_1, _2, _3, _4);
    }
    
    public static <T1, T2, T3, T4, T5> ImmutableTuple5<T1, T2, T3, T4, T5> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5) {
        return new ImmutableTuple5<>(_1, _2, _3, _4, _5);
    }
    
    public static <T1, T2, T3, T4, T5, T6> ImmutableTuple6<T1, T2, T3, T4, T5, T6> of(T1 _1, T2 _2, T3 _3, T4 _4, T5 _5, T6 _6) {
        return new ImmutableTuple6<>(_1, _2, _3, _4, _5, _6);
    }
    
    
    
    public static <T1, T2> ImmutableTuple2<T1, T2> of(Tuple2<T1, T2> tuple) {
        if (tuple instanceof ImmutableTuple2)
            return (ImmutableTuple2<T1, T2>)tuple;
        
        if (tuple == null)
            return new ImmutableTuple2<>(null, null);
        
        val _1 = tuple._1();
        val _2 = tuple._2();
        return new ImmutableTuple2<>(_1, _2);
    }

    public static <T1, T2, T3> ImmutableTuple3<T1, T2, T3> of(Tuple3<T1, T2, T3> tuple) {
        if (tuple instanceof ImmutableTuple3)
            return (ImmutableTuple3<T1, T2, T3>)tuple;
        
        if (tuple == null)
            return new ImmutableTuple3<>(null, null, null);
        
        val _1 = tuple._1();
        val _2 = tuple._2();
        val _3 = tuple._3();
        return new ImmutableTuple3<>(_1, _2, _3);
    }

    public static <T1, T2, T3, T4> ImmutableTuple4<T1, T2, T3, T4> of(Tuple4<T1, T2, T3, T4> tuple) {
        if (tuple instanceof ImmutableTuple4)
            return (ImmutableTuple4<T1, T2, T3, T4>)tuple;
        
        if (tuple == null)
            return new ImmutableTuple4<>(null, null, null, null);
        
        val _1 = tuple._1();
        val _2 = tuple._2();
        val _3 = tuple._3();
        val _4 = tuple._4();
        return new ImmutableTuple4<>(_1, _2, _3, _4);
    }

    public static <T1, T2, T3, T4, T5> ImmutableTuple5<T1, T2, T3, T4, T5> of(Tuple5<T1, T2, T3, T4, T5> tuple) {
        if (tuple instanceof ImmutableTuple5)
            return (ImmutableTuple5<T1, T2, T3, T4, T5>)tuple;
        
        if (tuple == null)
            return new ImmutableTuple5<>(null, null, null, null, null);
        
        val _1 = tuple._1();
        val _2 = tuple._2();
        val _3 = tuple._3();
        val _4 = tuple._4();
        val _5 = tuple._5();
        return new ImmutableTuple5<>(_1, _2, _3, _4, _5);
    }

    public static <T1, T2, T3, T4, T5, T6> ImmutableTuple6<T1, T2, T3, T4, T5, T6> of(Tuple6<T1, T2, T3, T4, T5, T6> tuple) {
        if (tuple instanceof ImmutableTuple6)
            return (ImmutableTuple6<T1, T2, T3, T4, T5, T6>)tuple;
        
        if (tuple == null)
            return new ImmutableTuple6<>(null, null, null, null, null, null);
        
        val _1 = tuple._1();
        val _2 = tuple._2();
        val _3 = tuple._3();
        val _4 = tuple._4();
        val _5 = tuple._5();
        val _6 = tuple._6();
        return new ImmutableTuple6<>(_1, _2, _3, _4, _5, _6);
    }
    
}
