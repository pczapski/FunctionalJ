package functionalj.annotations;

import java.util.Map;

// TODO - Make any data implements this.
public interface IRecord {
    
    // TODO - Add getSchema that has list of fields.
    public Map<String, Object> toMap();
    
}
