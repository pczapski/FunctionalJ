package functionalj.lens;

import static functionalj.lens.core.LensSpec.selfRead;
import static functionalj.lens.core.LensSpec.selfWrite;

import java.util.List;

import functionalj.lens.core.LensSpec;
import functionalj.lens.core.LensSpecParameterized;
import functionalj.lens.core.LensSpecParameterized2;
import functionalj.lens.core.LensType;
import functionalj.lens.core.LensUtils;
import functionalj.lens.lenses.AnyAccess;
import functionalj.lens.lenses.AnyLens;
import functionalj.lens.lenses.BooleanLens;
import functionalj.lens.lenses.ComparableLens;
import functionalj.lens.lenses.DoubleLens;
import functionalj.lens.lenses.IntegerLens;
import functionalj.lens.lenses.ListLens;
import functionalj.lens.lenses.LongLens;
import functionalj.lens.lenses.ObjectLens;
import functionalj.lens.lenses.StringLens;
import functionalj.lens.lenses.Tuple2Lens;
import functionalj.tuple.Tuple2;

@SuppressWarnings("javadoc")
public interface Access {
    
    public static final AnyLens<Object, Object> theObject  = AnyLens    .of(LensSpec.of(Object.class));
    public static final BooleanLens<Boolean>    theBoolean = BooleanLens.of(LensSpec.of(Boolean.class));
    public static final StringLens<String>      theString  = StringLens .of(LensSpec.of(String.class));
    public static final IntegerLens<Integer>    theInteger = IntegerLens.of(LensSpec.of(Integer.class));
    public static final LongLens<Long>          theLong    = LongLens   .of(LensSpec.of(Long.class));
    public static final DoubleLens<Double>      theDouble  = DoubleLens .of(LensSpec.of(Double.class));

    public static final AnyLens<Object, Object> $O = theObject;
    public static final BooleanLens<Boolean>    $B = theBoolean;
    public static final StringLens<String>      $S = theString;
    public static final IntegerLens<Integer>    $I = theInteger;
    public static final LongLens<Long>          $L = theLong;
    public static final DoubleLens<Double>      $D = theDouble;
    
    public static final TheListLens   theList   = new TheListLens();
    public static final TheTuple2Lens theTuple2 = new TheTuple2Lens();
    
    
    public static <T> AnyLens<T, T> theItem() {
        return AnyLens.of(LensSpec.of((T item) -> item, (T host, T newItem) -> newItem));
    }
    public static <T> ObjectLens<T, T> theObject() {
        return ObjectLens.of(LensSpec.of((T item) -> item, (T host, T newItem) -> newItem));
    }
    public static <T extends Comparable<T>> ComparableLens<T, T> theComparable() {
        return ComparableLens.of(LensSpec.of((T item) -> item, (T host, T newItem) -> newItem));
    }
    public static <T1, T2, 
            T1ACCESS extends AnyAccess<Tuple2<T1, T2>, T1>, T2ACCESS extends AnyAccess<Tuple2<T1, T2>, T2>, 
            T1LENS   extends AnyLens<Tuple2<T1, T2>, T1>,   T2LENS   extends AnyLens<Tuple2<T1, T2>, T2>>
        Tuple2Lens<Tuple2<T1,T2>, T1, T2, T1LENS, T2LENS> theTupleOf(
             LensType<Tuple2<T1, T2>, T1, T1ACCESS, T1LENS> t1Type,
             LensType<Tuple2<T1, T2>, T2, T2ACCESS, T2LENS> t2Type) {
        return theTuple2.of(t1Type, t2Type);
    }
    
    
    
    
    //== Internal use only ==
    
    public static class TheListLens implements ListLens<List<?>, Object, ObjectLens<List<?>, Object>> {
        
        private static final LensSpecParameterized<List<?>, List<?>, Object, ObjectLens<List<?>, Object>> 
                common = LensUtils.createLensSpecParameterized(selfRead(), selfWrite(), ObjectLens::of);
        
        public <T, SA extends AnyAccess<List<T>, T>, SL extends AnyLens<List<T>, T>> 
                ListLens<List<T>, T, SL> of(LensType<List<T>, T, SA, SL> type) {
            LensSpecParameterized<List<T>, List<T>, T, SL> spec
                    = LensUtils.createLensSpecParameterized(LensSpec.selfRead(), LensSpec.selfWrite(), s -> type.newLens(s));
            ListLens<List<T>, T, SL> listLens = ListLens.of(spec);
            return listLens;
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public LensSpecParameterized<List<?>, List<Object>, Object, ObjectLens<List<?>, Object>> lensSpecParameterized() {
            return (LensSpecParameterized)common;
        }
    }
    
    //-- Tuple2 --
    
    public static class TheTuple2Lens implements Tuple2Lens<Tuple2<Object,Object>, Object, Object, 
            ObjectLens<Tuple2<Object,Object>, Object>, ObjectLens<Tuple2<Object,Object>, Object>> {
        
        // I am tired .... just goes with this.
        private static final LensSpecParameterized2<Tuple2<Object, Object>, Tuple2<Object, Object>, Object, Object, ObjectLens<Tuple2<Object, Object>, Object>, ObjectLens<Tuple2<Object, Object>, Object>> 
                common = new LensSpecParameterized2<Tuple2<Object,Object>, Tuple2<Object,Object>, Object, Object, ObjectLens<Tuple2<Object,Object>,Object>, ObjectLens<Tuple2<Object,Object>,Object>>() {
                    @Override
                    public LensSpec<Tuple2<Object, Object>, Tuple2<Object, Object>> getSpec() {
                        return LensSpec.of(selfRead(), selfWrite());
                    }
                    @Override
                    public ObjectLens<Tuple2<Object, Object>, Object> createSubLens1(
                            LensSpec<Tuple2<Object, Object>, Object> subSpec) {
                        return ObjectLens.of(subSpec);
                    }
                    @Override
                    public ObjectLens<Tuple2<Object, Object>, Object> createSubLens2(
                            LensSpec<Tuple2<Object, Object>, Object> subSpec) {
                        return ObjectLens.of(subSpec);
                    }
                };
        
        public <T1, T2, 
                       T1ACCESS extends AnyAccess<Tuple2<T1, T2>, T1>, T2ACCESS extends AnyAccess<Tuple2<T1, T2>, T2>, 
                       T1LENS   extends AnyLens<Tuple2<T1, T2>, T1>,   T2LENS   extends AnyLens<Tuple2<T1, T2>, T2>>
                Tuple2Lens<Tuple2<T1,T2>, T1, T2, T1LENS, T2LENS> of(
                        LensType<Tuple2<T1, T2>, T1, T1ACCESS, T1LENS> t1Type,
                        LensType<Tuple2<T1, T2>, T2, T2ACCESS, T2LENS> t2Type) {
            return Tuple2Lens.of(selfRead(), selfWrite(), t1Type, t2Type);
        }
        
        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public LensSpecParameterized2<Tuple2<Object, Object>, Tuple2<Object, Object>, Object, Object, 
            ObjectLens<Tuple2<Object, Object>, Object>, ObjectLens<Tuple2<Object, Object>, Object>> lensSpecParameterized2() {
            return (LensSpecParameterized2)common;
        }
        
    }
    
}
