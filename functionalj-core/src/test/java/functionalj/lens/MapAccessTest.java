package functionalj.lens;

import static functionalj.lens.Access.theString;
import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.junit.Test;

import functionalj.lens.core.AccessParameterized2;
import functionalj.lens.lenses.IntegerAccess;
import functionalj.lens.lenses.MapAccess;
import functionalj.lens.lenses.StringAccess;
import lombok.val;

public class MapAccessTest {
    
    @Test
    public void testMapAccess() {
        val mapAccess = new MapAccess<
                                Map<String, Integer>, String, Integer, 
                                StringAccess<Map<String, Integer>>, IntegerAccess<Map<String, Integer>>>() {
            @Override
            public AccessParameterized2<Map<String, Integer>, Map<String, Integer>, String, Integer, StringAccess<Map<String, Integer>>, IntegerAccess<Map<String, Integer>>> accessParameterized2() {
                return new AccessParameterized2<Map<String,Integer>, Map<String,Integer>, String, Integer, StringAccess<Map<String,Integer>>, IntegerAccess<Map<String,Integer>>>() {
                    @Override
                    public Map<String, Integer> applyUnsafe(Map<String, Integer> host) throws Exception {
                        return host;
                    }
                    @Override
                    public StringAccess<Map<String, Integer>> createSubAccess1(
                            Function<Map<String, Integer>, String> accessToParameter) {
                        return map -> accessToParameter.apply(map);
                    }
                    @Override
                    public IntegerAccess<Map<String, Integer>> createSubAccess2(
                            Function<Map<String, Integer>, Integer> accessToParameter) {
                        return map -> accessToParameter.apply(map);
                    }
                    @Override
                    public StringAccess<Map<String, Integer>> createSubAccessFromHost1(
                            Function<Map<String, Integer>, String> accessToParameter) {
                        return accessToParameter::apply;
                    }
                    @Override
                    public IntegerAccess<Map<String, Integer>> createSubAccessFromHost2(
                            Function<Map<String, Integer>, Integer> accessToParameter) {
                        return accessToParameter::apply;
                    }
                };
            }
        };
        
        val map = new LinkedHashMap<String, Integer>();
        map.put("name1", 2);
        map.put("name2", 4);
        assertEquals("[name1, name2]",     mapAccess.keys()   .apply(map).toString());
        assertEquals("[2, 4]",             mapAccess.values() .apply(map).toString());
        assertEquals("[name1=2, name2=4]", mapAccess.entries().apply(map).toString());
        
        assertEquals("2", mapAccess.get("name1").apply(map).toString());
        assertEquals("4", mapAccess.get("name2").apply(map).toString());
        
        
        assertEquals("[name1=2]", mapAccess.filterEntries(entry -> entry.getKey().endsWith("1")).apply(map).toString());
        assertEquals("[name1=2]", mapAccess.filter(theString.thatEndsWith("1")).apply(map).toString());
    }
    
}
