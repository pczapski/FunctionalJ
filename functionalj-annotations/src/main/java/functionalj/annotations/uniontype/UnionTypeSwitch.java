package functionalj.annotations.uniontype;

import java.util.function.Function;
import java.util.function.Supplier;

import lombok.val;

public abstract class UnionTypeSwitch<D, T> {
    protected final D                      $value;
    protected final Function<? super D, T> $action;
    protected UnionTypeSwitch(D theValue, Function<? super D, T> theAction) {
        this.$value  = theValue;
        this.$action = theAction;
    }
    
    public T orElse(T elseValue) {
        return ($action != null)
                ? $action.apply($value)
                : elseValue;
    }
    
    public T orGet(Supplier<T> valueSupplier) {
        return ($action != null)
                ? $action.apply($value)
                : valueSupplier.get();
    }
    public T orGet(Function<? super D, T> valueMapper) {
        @SuppressWarnings("unchecked")
        val newAction = (Function<? super D, T>)(($action != null) ? $action : valueMapper);
        return newAction.apply($value);
    }
    public T orElseGet(Supplier<T> valueSupplier) {
        return orGet(valueSupplier);
    }
    public T orElseGet(Function<? super D, T> valueMapper) {
        return orGet(valueMapper);
    }
    
    public static class UnionTypeSwitchData<D, T> {
        protected final D              value;
        protected final Function<D, T> action;
        
        public UnionTypeSwitchData(D value) {
            this(value, null);
        }
        public UnionTypeSwitchData(D value, Function<D, T> action) {
            this.value  = value;
            this.action = action;
        }
        public D                         value()                           { return value; }
        public Function<D, T>            action()                          { return action; }
        public UnionTypeSwitchData<D, T> withValue(D value)                { return new UnionTypeSwitchData<>(value, action); }
        public UnionTypeSwitchData<D, T> withAction(Function<D, T> action) { return new UnionTypeSwitchData<>(value, action); }
    }
}
