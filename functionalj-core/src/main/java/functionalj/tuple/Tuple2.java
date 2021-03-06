package functionalj.tuple;

import static functionalj.function.Func.it;

import java.lang.reflect.Array;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import functionalj.function.Absent;
import functionalj.function.Func2;
import functionalj.list.FuncList;
import functionalj.map.FuncMap;
import functionalj.map.ImmutableMap;
import functionalj.pipeable.Pipeable;
import lombok.val;

@SuppressWarnings("javadoc")
public interface Tuple2<T1, T2> extends Pipeable<Tuple2<T1, T2>> {
    
    public static <T1, T2> Tuple2<T1, T2> of(Map.Entry<? extends T1, ? extends T2> entry) {
        return (entry == null)
                ? new ImmutableTuple2<>(null, null)
                : new ImmutableTuple2<>(entry);
    }
    public static <T1, T2> Tuple2<T1, T2> of(T1 t1, T2 t2) {
        return new ImmutableTuple2<>(t1, t2);
    }
    
    public T1 _1();
    public T2 _2();
    
    @Override
    public default Tuple2<T1, T2> __data() {
        return this;
    }
    
    public default Tuple2<T2, T1> swap() {
        return Tuple.of(_2(), _1());
    }
    
    public default ImmutableTuple2<T1, T2> toImmutableTuple() {
        return ImmutableTuple.of(this);
    }
    
    public default Object[] toArray() {
        val _1 = _1();
        val _2 = _2();
        return new Object[] { _1, _2 };
    }
    
    public default <T> T[] toArray(Class<T> type) {
        val _1 = _1();
        val _2 = _2();
        val array = Array.newInstance(type, 2);
        Array.set(array, 0, _1);
        Array.set(array, 1, _2);
        @SuppressWarnings("unchecked")
        val toArray = (T[])array;
        return toArray;
    }
    
    public default FuncList<Object> toList() {
        val _1 = _1();
        val _2 = _2();
        return FuncList.of(_1, _2);
    }
    
    public default <K> FuncMap<K, Object> toMap(K k1, K k2) {
        val e1 = (k1 != null) ? ImmutableTuple.of(k1, (Object)_1()) : null;
        val e2 = (k2 != null) ? ImmutableTuple.of(k2, (Object)_2()) : null;
        return ImmutableMap.ofEntries(e1, e2);
    }
    
    //== Map ==
    
    public default <NT1> Tuple2<NT1, T2> map1(Function<? super T1, NT1> mapper) {
        return map(mapper, it());
    }
    
    public default <NT2> Tuple2<T1, NT2> map2(Function<? super T2, NT2> mapper) {
        return map(it(), mapper);
    }
    
    public default <NT1, NT2> Tuple2<NT1, NT2> map(BiFunction<? super T1, ? super T2, Tuple2<NT1, NT2>> mapper) {
        val _1 = _1();
        val _2 = _2();
        return mapper.apply(_1, _2);
    }
    
    public default <NT1, NT2> Tuple2<NT1, NT2> map(
            Function<? super T1, NT1> mapper1,
            Function<? super T2, NT2> mapper2) {
        return map(mapper1, mapper2, Tuple::of);
    }
    public default <NT1, NT2, T> T map(
            Function<? super T1, NT1> mapper1, 
            Function<? super T2, NT2> mapper2, 
            BiFunction<? super NT1, ? super NT2, T> mapper) {
        val _1 = _1();
        val _2 = _2();
        val n1 = mapper1.apply(_1);
        val n2 = mapper2.apply(_2);
        return mapper.apply(n1, n2);
    }
    
    public default <NT2> Tuple2<T1, NT2> map(Absent absent1, Function<? super T2, NT2> mapper2) {
        val _1 = _1();
        val _2 = _2();
        val n1 = _1;
        val n2 = mapper2.apply(_2);
        return Tuple.of(n1, n2);
    }
    
    public default <NT1> Tuple2<NT1, T2> map(Function<? super T1, NT1> mapper1, Absent absent2) {
        val _1 = _1();
        val _2 = _2();
        val n1 = mapper1.apply(_1);
        val n2 = _2;
        return Tuple.of(n1, n2);
    }
    
    //== Reduce ==
    
    public default <TARGET> TARGET reduce(Func2<T1, T2, TARGET> reducer) {
        val _1     = _1();
        val _2     = _2();
        val target = reducer.apply(_1, _2);
        return target;
    }
    
    //== drop ==
    
    public default T1 drop() {
        val _1 = _1();
        return _1;
    }
    
}
