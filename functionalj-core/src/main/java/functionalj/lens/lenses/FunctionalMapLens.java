package functionalj.lens.lenses;

import java.util.function.Function;

import functionalj.lens.core.AccessParameterized2;
import functionalj.lens.core.LensSpec;
import functionalj.lens.core.LensSpecParameterized2;
import functionalj.lens.core.LensUtils;
import functionalj.lens.core.WriteLens;
import functionalj.types.FunctionalMap;
import lombok.val;

public interface FunctionalMapLens<HOST, KEY, VALUE, 
                            KEYLENS   extends AnyLens<HOST,KEY>, 
                            VALUELENS extends AnyLens<HOST,VALUE>>
                    extends
                        ObjectLens<HOST, FunctionalMap<KEY, VALUE>>,
                        FunctionalMapAccess<HOST, KEY, VALUE, KEYLENS, VALUELENS> {
    
    public static <HOST, KEY, VALUE, KEYLENS extends AnyLens<HOST,KEY>, VALUELENS extends AnyLens<HOST,VALUE>>
            FunctionalMapLens<HOST, KEY, VALUE, KEYLENS, VALUELENS> of(
                    Function<HOST,  FunctionalMap<KEY, VALUE>>           read,
                    WriteLens<HOST, FunctionalMap<KEY, VALUE>>           write,
                    Function<LensSpec<HOST, KEY>,   KEYLENS>   keyLensCreator,
                    Function<LensSpec<HOST, VALUE>, VALUELENS> valueLensCreator) {
        val spec = LensUtils.createFunctionalMapLensSpec(read, write, keyLensCreator, valueLensCreator);    
        return ()->spec;
    }
    
    public LensSpecParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS> lensSpecParameterized2();
    
    @Override
    public default AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYLENS, VALUELENS> accessParameterized2() {
        return lensSpecParameterized2();
    }

    @Override
    public default LensSpec<HOST, FunctionalMap<KEY, VALUE>> lensSpec() {
        return lensSpecParameterized2().getSpec();
    }

    @Override
    public default FunctionalMap<KEY, VALUE> apply(HOST host) {
        return ObjectLens.super.apply(host);
    }
    
    // TODO - Fix this.
//    
//    public default VALUELENS get(KEY key) {
//        WriteLens<Map<KEY, VALUE>, VALUE> write = (map, value) -> {
//            val newMap = new LinkedHashMap<KEY, VALUE>();
//            newMap.putAll(map);
//            newMap.put(key, value);
//            return newMap;
//        };
//        Function<Map<KEY, VALUE>, VALUE> read = map -> {
//            return map.get(key);
//        };
//        return LensUtils.createSubLens(this, read, write, lensSpecParameterized2()::createSubLens2);
//    }
//    
//    public default Function<HOST, HOST> changeTo(Predicate<KEY> checker, Function<VALUE, VALUE> mapper) {
//        val mapEntry = Func1.of((Map.Entry<KEY, VALUE> each) ->{
//            val key   = each.getKey();
//            val value = each.getValue();
//            if (!checker.test(key)) 
//                return each;
//            
//            val newValue = mapper.apply(value);
//            return (Map.Entry<KEY, VALUE>)new ImmutableTuple2<KEY, VALUE>(key, newValue);
//        });
//        
//        val newMap = new LinkedHashMap<KEY, VALUE>();
//        Consumer<? super Entry<KEY, VALUE>> transformEntry = entry -> {
//            val key   = entry.getKey();
//            val value = entry.getValue();
//            if (!checker.test(key)) 
//                newMap.put(key, value);
//            else {
//                val newValue = mapper.apply(value);
//                newMap.put(key, newValue);
//            }
//        };
//        return host -> {
//            apply(host).entrySet().stream()
//                    .map    (mapEntry)
//                    .forEach(transformEntry);
//            val newHost = apply(host, newMap);
//            return newHost;
//        };
//    }
//
//    
//    public default Function<HOST, HOST> changeTo(BiPredicate<KEY, VALUE> checker, Function<VALUE, VALUE> mapper) {
//        val mapEntry = Func1.of((Map.Entry<KEY, VALUE> each) ->{
//            val key   = each.getKey();
//            val value = each.getValue();
//            if (!checker.test(key, value)) 
//                return each;
//            
//            val newValue = mapper.apply(value);
//            return (Map.Entry<KEY, VALUE>)new ImmutableTuple2<KEY, VALUE>(key, newValue);
//        });
//        
//        return host -> {
//            val newMap = new LinkedHashMap<KEY, VALUE>();
//            Consumer<? super Entry<KEY, VALUE>> transformEntry = entry -> {
//                val key   = entry.getKey();
//                val value = entry.getValue();
//                if (!checker.test(key, value)) 
//                    newMap.put(key, value);
//                else {
//                    val newValue = mapper.apply(value);
//                    newMap.put(key, newValue);
//                }
//            };
//            apply(host).entrySet().stream()
//                    .map    (mapEntry)
//                    .forEach(transformEntry);
//            val newHost = apply(host, newMap);
//            return newHost;
//        };
//    }
}
