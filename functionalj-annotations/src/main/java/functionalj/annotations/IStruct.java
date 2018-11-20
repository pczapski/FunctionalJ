package functionalj.annotations;

import java.util.Map;

import functionalj.annotations.struct.generator.Getter;
import lombok.val;

public interface IStruct {
    
    public Map<String, Object> toMap();
    public Map<String, Getter> getSchema();
    
    public static Object toMapValueObject(Object data) {
        return (data instanceof IStruct) ? ((IStruct)data).toMap() : (Object)data;
    }
    
    public static <S extends IStruct> S fromMap(Map<String, Object> map, Class<S> clazz) {
        try {
            val method = clazz.getMethod("fromMap", Map.class);
            val struct = method.invoke(clazz, map);
            return clazz.cast(struct);
        } catch (Exception cause) {
            throw new StructConversionException(cause);
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T fromMapValue(Object obj, Getter getter) {
        val type         = getter.getType();
        val defaultValue = getter.getDefaultTo();
        if ((obj == null) && ((defaultValue == DefaultValue.UNSPECIFIED) || (defaultValue == DefaultValue.NULL)))
            return null;
        
        Class clzz = type.toClass();
        if ((obj instanceof Map) && IStruct.class.isAssignableFrom(clzz)) {
            return (T)fromMap((Map)obj, clzz);
        }
        
        if (obj != null)
            return (T)obj;
        
        val value = DefaultValue.defaultValue(type, defaultValue);
        return (T)value;
    }
    
}
