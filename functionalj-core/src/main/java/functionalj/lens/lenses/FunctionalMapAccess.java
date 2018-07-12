package functionalj.lens.lenses;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toSet;

import functionalj.functions.Func1;
import functionalj.lens.core.AccessParameterized;
import functionalj.lens.core.AccessParameterized2;
import functionalj.lens.lenses.MapAccess.Helper;
import functionalj.types.FunctionalMap;
import functionalj.types.Tuple2;
import lombok.val;


@FunctionalInterface
public interface FunctionalMapAccess<HOST, KEY, VALUE, 
                            KEYACCESS extends AnyAccess<HOST,KEY>, 
                            VALUEACCESS extends AnyAccess<HOST,VALUE>>
                    extends AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> {

    public AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> accessParameterized2();
    
    @Override
    public default FunctionalMap<KEY, VALUE> apply(HOST host) {
        return accessParameterized2().apply(host);
    }

    @Override
    public default KEYACCESS createSubAccess1(Function<FunctionalMap<KEY, VALUE>, KEY> accessToParameter) {
        return keyAccess(accessToParameter);
    }

    @Override
    public default VALUEACCESS createSubAccess2(Function<FunctionalMap<KEY, VALUE>, VALUE> accessToParameter) {
        return valueAccess(accessToParameter);
    }

    @Override
    public default KEYACCESS createSubAccessFromHost1(Function<HOST, KEY> accessToParameter) {
        return accessParameterized2().createSubAccessFromHost1(accessToParameter);
    }
    
    @Override
    public default VALUEACCESS createSubAccessFromHost2(Function<HOST, VALUE> accessToParameter) {
        return accessParameterized2().createSubAccessFromHost2(accessToParameter);
    }
    

    public default KEYACCESS keyAccess(Function<FunctionalMap<KEY, VALUE>, KEY> accessToParameter) {
        return accessParameterized2().createSubAccess1(accessToParameter);
    }

    public default VALUEACCESS valueAccess(Function<FunctionalMap<KEY, VALUE>, VALUE> accessToParameter) {
        return accessParameterized2().createSubAccess2(accessToParameter);
    }
    
    public default VALUEACCESS get(KEY key) {
        return valueAccess(map -> map.get(key));
    }
    public default VALUEACCESS getOrDefault(KEY key, VALUE defaultValue) {
        return valueAccess(map -> map.getOrDefault(key, defaultValue));
    }
    
    // TODO - Uncomment.
    
//    
//    public default IntegerAccess<HOST> size() {
//        return intAccess(0, map -> map.size());
//    }
//    
//    public default BooleanAccess<HOST> isEmpty() {
//        return booleanAccess(true, map -> map.isEmpty());
//    }
//    
//    public default BooleanAccess<HOST> containsKey(KEY key) {
//        return booleanAccess(false, map -> map.containsKey(key));
//    }
//    public default BooleanAccess<HOST> containsKey(Predicate<KEY> keyPredicate) {
//        return booleanAccess(false, map -> map.keySet().stream().anyMatch(keyPredicate));
//    }
//    
//    public default BooleanAccess<HOST> containsValue(VALUE value) {
//        return booleanAccess(false, map -> map.containsValue(value));
//    }
//    public default BooleanAccess<HOST> containsValue(Predicate<VALUE> valuePredicate) {
//        return booleanAccess(false, map -> map.values().stream().anyMatch(valuePredicate));
//    }
//    
//    public default BooleanAccess<HOST> containsEntry(Predicate<Map.Entry<KEY, VALUE>> entryPredicate) {
//        return booleanAccess(false, map -> map.entrySet().stream().anyMatch(entryPredicate));
//    }
//    public default BooleanAccess<HOST> containsEntry(BiPredicate<KEY, VALUE> entryBiPredicate) {
//        return containsEntry(entry -> entryBiPredicate.test(entry.getKey(), entry.getValue()));
//    }
//    
//    public default CollectionAccess<HOST, Collection<Map.Entry<KEY, VALUE>>, Map.Entry<KEY, VALUE>,
//                    MapEntryAccess<HOST, Map.Entry<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>> 
//            entries() {
//        val entryCollectionSpec = Helper.createEntryCollectionSpec(accessParameterized2(), map -> map.entrySet());
//        return () -> entryCollectionSpec;
//    }
//    public default CollectionAccess<HOST, Collection<Map.Entry<KEY, VALUE>>, Map.Entry<KEY, VALUE>, 
//                    MapEntryAccess<HOST, Map.Entry<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>> 
//            filterEntries(Predicate<Map.Entry<KEY, VALUE>> entryPredicate) {
//        val entryCollectionSpec = Helper.createEntryCollectionSpec(accessParameterized2(), map->{
//            return map.entrySet().stream().filter(entryPredicate).collect(toSet());
//        });
//        return () -> entryCollectionSpec;
//    }
//    public default CollectionAccess<HOST, Collection<Map.Entry<KEY, VALUE>>, Map.Entry<KEY, VALUE>, 
//                    MapEntryAccess<HOST, Map.Entry<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>> 
//            filterEntries(BiPredicate<KEY, VALUE> entryBiPredicate) {
//        val entryCollectionSpec = Helper.createEntryCollectionSpec(accessParameterized2(), map->{
//            return map.entrySet().stream()
//                    .filter(entry -> entryBiPredicate.test(entry.getKey(), entry.getValue()))
//                    .collect(toSet());
//        });
//        return () -> entryCollectionSpec;
//    }
//    public default CollectionAccess<HOST, Collection<Map.Entry<KEY, VALUE>>, Map.Entry<KEY, VALUE>, 
//                    MapEntryAccess<HOST, Map.Entry<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>> 
//            filter(Predicate<KEY> keyPredicate) {
//        val entryCollectionSpec = Helper.createEntryCollectionSpec(accessParameterized2(), map->{
//            return map.entrySet().stream()
//                    .filter(entry->keyPredicate.test(entry.getKey()))
//                    .collect(toSet());
//        });
//        return () -> entryCollectionSpec;
//    }
//    
//    public default CollectionAccess<HOST, Collection<KEY>, KEY, KEYACCESS> keys() {
//        val keyCollectionSpec = Helper.createKeyCollectionSpec(accessParameterized2(), Map::keySet);
//        return () -> keyCollectionSpec;
//    }
//    
//    public default CollectionAccess<HOST, Collection<VALUE>, VALUE, VALUEACCESS> values() {
//        val valueCollectionSpec = Helper.createValueCollectionSpec(accessParameterized2(), Map::values);
//        return () -> valueCollectionSpec;
//    }
    
    //== Just some helpers -- ignore this ==
    
    public static class Helper {
        
        public static <HOST, KEY, VALUE, KEYACCESS extends AnyAccess<HOST,KEY>, VALUEACCESS extends AnyAccess<HOST,VALUE>>
            AccessParameterized<HOST, Collection<KEY>, KEY, KEYACCESS> createKeyCollectionSpec(
                    AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> spec,
                    Function<FunctionalMap<KEY, VALUE>, Collection<KEY>>                                      getKeys) {
            return new AccessParameterized<HOST, Collection<KEY>, KEY, KEYACCESS>() {
                @Override
                public Collection<KEY> apply(HOST host) {
                    return getKeys.apply(spec.apply(host));
                }
                @Override
                public KEYACCESS createSubAccessFromHost(Function<HOST, KEY> accessToParameter) {
                    return spec.createSubAccessFromHost1(accessToParameter);
                }
            };
        }

        public static <HOST, KEY, VALUE, KEYACCESS extends AnyAccess<HOST,KEY>, VALUEACCESS extends AnyAccess<HOST,VALUE>>
            AccessParameterized<HOST, Collection<VALUE>, VALUE, VALUEACCESS> createValueCollectionSpec(
                    AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> spec,
                    Function<FunctionalMap<KEY, VALUE>, Collection<VALUE>> getValues) {
            return new AccessParameterized<HOST, Collection<VALUE>, VALUE, VALUEACCESS>() {
                @Override
                public Collection<VALUE> apply(HOST host) {
                    return getValues.apply(spec.apply(host));
                }
                @Override
                public VALUEACCESS createSubAccessFromHost(Function<HOST, VALUE> accessToParameter) {
                    return spec.createSubAccessFromHost2(accessToParameter);
                }
            };
        }

        public static <HOST, KEY, VALUE, KEYACCESS extends AnyAccess<HOST,KEY>, VALUEACCESS extends AnyAccess<HOST,VALUE>>
            AccessParameterized<HOST, Collection<Tuple2<KEY, VALUE>>, Tuple2<KEY, VALUE>, FunctionalMapEntryAccess<HOST, Tuple2<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>>
            createEntryCollectionSpec(
                    AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> spec,
                    Function<FunctionalMap<KEY, VALUE>, Collection<Tuple2<KEY, VALUE>>> accessEntrySet) {
            val access = new AccessParameterized<HOST, 
                                Collection<Tuple2<KEY, VALUE>>, 
                                Tuple2<KEY, VALUE>, 
                                FunctionalMapEntryAccess<HOST, Tuple2<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>>() {
                
                            @Override
                            public Collection<Tuple2<KEY, VALUE>> apply(HOST host) {
                                return accessEntrySet.apply(spec.apply(host));
                            }
                            @Override
                            public FunctionalMapEntryAccess<HOST, Tuple2<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> createSubAccessFromHost(
                                    Function<HOST, Tuple2<KEY, VALUE>> accessToParameter) {
                                // TODO - generalized this or just move it to other place.
                                return new FunctionalMapEntryAccess<HOST, Tuple2<KEY,VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>() {
                                    @Override
                                    public AccessParameterized2<HOST, Tuple2<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> accessParameterized2() {
                                        AccessParameterized2<HOST, Tuple2<KEY,VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> accessParameterized2 = new AccessParameterized2<HOST, Tuple2<KEY,VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>() {
                                            @Override
                                            public Tuple2<KEY, VALUE> apply(HOST host) {
                                                val entry = accessToParameter.apply(host);
                                                return entry;
                                            }
                                            @Override
                                            public KEYACCESS createSubAccessFromHost1(Function<HOST, KEY> accessToParameter) {
                                                return spec.createSubAccessFromHost1(accessToParameter);
                                            }
                                            @Override
                                            public VALUEACCESS createSubAccessFromHost2(Function<HOST, VALUE> accessToParameter) {
                                                return spec.createSubAccessFromHost2(accessToParameter);
                                            }
                                        };
                                        return accessParameterized2;
                                    }
                                };
                            }
                        };
            return access;
        }

        public static <HOST, KEY, VALUE, KEYACCESS extends AnyAccess<HOST,KEY>, VALUEACCESS extends AnyAccess<HOST,VALUE>>
            AccessParameterized2<HOST, FunctionalMap.Entry<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> createEntrySpec(
                AccessParameterized2<HOST, FunctionalMap<KEY, VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> mapAccessSpec,
                Func1<FunctionalMap<KEY, VALUE>, FunctionalMap.Entry<KEY, VALUE>>                                   accessEntry) {
            AccessParameterized2<HOST, FunctionalMap.Entry<KEY,VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS> entrySpec
            = new AccessParameterized2<HOST, FunctionalMap.Entry<KEY,VALUE>, KEY, VALUE, KEYACCESS, VALUEACCESS>() {
                @Override
                public Entry<KEY, VALUE> apply(HOST host) {
                    val map   = mapAccessSpec.apply(host);
                    val entry = accessEntry.apply(map);
                    return entry;
                }
                
                @Override
                public KEYACCESS createSubAccessFromHost1(Function<HOST, KEY> accessToParameter) {
                    return mapAccessSpec.createSubAccessFromHost1(accessToParameter);
                }
                @Override
                public VALUEACCESS createSubAccessFromHost2(Function<HOST, VALUE> accessToParameter) {
                    return mapAccessSpec.createSubAccessFromHost2(accessToParameter);
                }
            };
            return entrySpec;
        }
    }
    
}