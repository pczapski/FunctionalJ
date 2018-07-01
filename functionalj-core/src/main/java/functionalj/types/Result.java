package functionalj.types;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import functionalj.kinds.Comonad;
import functionalj.kinds.Filterable;
import functionalj.kinds.Functor;
import functionalj.kinds.Monad;
import funtionalj.failable.FailableBiFunction;
import funtionalj.failable.FailableFunc0;
import funtionalj.failable.FailableFunction;
import funtionalj.failable.FailableSupplier;
import lombok.val;
import nawaman.nullablej.nullable.Nullable;


@FunctionalInterface
public interface Result<DATA>
                    extends 
                        Functor<Result<?>, DATA>, 
                        Monad<Result<?>, DATA>, 
                        Comonad<Result<?>, DATA>,
                        Filterable<Result<?>, DATA>,
                        Tuple2<DATA, Exception>,
                        Nullable<DATA> {
    
    //TODO - Make it dynamic like List
    
    public Tuple2<DATA, Exception> asTuple();
    
    public default DATA getValue() {
        return asTuple()._1();
    }
    
    public default Exception getException() {
        return asTuple()._2();
    }
    
    @Override
    public default DATA get() {
        return getValue();
    }
    
    @Override
    public default DATA _extract() {
        return getValue();
    }
    
    @Override
    public default DATA _1() {
        return asTuple()._1();
    }
    @Override
    public default Exception _2() {
        return asTuple()._2();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public default <TARGET> Result<TARGET> _map(Function<? super DATA, TARGET> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((Supplier<TARGET>)()->{
            return mapper.apply(value);
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public default <TARGET> Result<TARGET> _flatMap(Function<? super DATA, Monad<Result<?>, TARGET>> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((Supplier<TARGET>)()->{
            Result<TARGET> newResult = (ImmutableResult<TARGET>)mapper.apply(value);
            return newResult.getValue();
        });
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> map(BiFunction<DATA, Exception, TARGET> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((Supplier<TARGET>)()->{
            val exception = getException();
            return mapper.apply(value, exception);
        });
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> flatMap(BiFunction<DATA, Exception, Monad<Result<?>, TARGET>> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((Supplier<TARGET>)()->{
            val exception = getException();
            Result<TARGET> newResult = (Result<TARGET>)mapper.apply(value, exception);
            return newResult.getValue();
        });
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> map(FailableFunction<DATA, TARGET> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            return mapper.apply(value);
        });
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> map(FailableBiFunction<DATA, Exception, TARGET> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            val exception = getException();
            return mapper.apply(value, exception);
        });
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> flatMap(FailableFunction<DATA, Monad<Result<?>, TARGET>> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            Result<TARGET> newResult = (ImmutableResult<TARGET>)mapper.apply(value);
            return newResult.getValue();
        });
    }
    @Override
    public default <TARGET> Result<TARGET> map(Function<? super DATA, TARGET> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            return mapper.apply(value);
        });
    }
    
    @Override
    public default <TARGET> Result<TARGET> flatMap(Function<? super DATA, ? extends Nullable<TARGET>> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            Result<TARGET> newResult = (ImmutableResult<TARGET>)mapper.apply(value);
            return newResult.getValue();
        });
    }
    @Override
    public default Result<DATA> peek(Consumer<? super DATA> theConsumer) {
        Nullable.super.peek(theConsumer);
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public default <TARGET> Result<TARGET> flatMap(FailableBiFunction<DATA, Exception, Monad<Result<?>, TARGET>> mapper) {
        DATA value = getValue();
        if (value == null)
            return (ImmutableResult<TARGET>)this;
        
        return ImmutableResult.from((FailableSupplier<TARGET>)()->{
            val exception = getException();
            Result<TARGET> newResult = (ImmutableResult<TARGET>)mapper.apply(value, exception);
            return newResult.getValue();
        });
    }
    
    public default boolean isValue() {
        val exception = getException();
        return exception == null;
    }
    
    public default boolean isNotNull() {
        val exception = getException();
        if (exception != null)
            return false;
        DATA value = getValue();
        return (value != null);
    }
    
    public default boolean isNull() {
        val exception = getException();
        if (exception != null)
            return false;
        DATA value = getValue();
        return (value == null);
    }
    
    public default boolean isException() {
        val exception = getException();
        return exception != null;
    }
    
    public default Result<DATA> ensureNotNull() {
        DATA value = getValue();
        if (value != null)
            return this;
        return ImmutableResult.of(null, new NullPointerException());
    }
    
    public default Result<DATA> makeNullable() {
        val exception = getException();
        if (exception instanceof NullPointerException)
            return ImmutableResult.ofNull();
        
        return this;
    }
    
    public default Result<DATA> filter(Predicate<? super DATA> theCondition) {
        DATA value = get();
        if (value == null)
            return this;
        
        val isPass = theCondition.test(value);
        if (!isPass)
            return ImmutableResult.ofNull();
        
        return this;
    }
    
    @Override
    public default Result<DATA> _filter(Function<? super DATA, Boolean> predicate) {
        return filter(predicate::apply);
    }
    
    public default Result<DATA> otherwise(DATA otherwiseValue) {
        DATA value = getValue();
        if (value == null)
            return ImmutableResult.of(otherwiseValue, null);
        return this;
    }
    
    public default Result<DATA> otherwiseGet(Supplier<DATA> otherwiseSupplier) {
        DATA value = getValue();
        if (value == null)
            return this;
        
        return ImmutableResult.from((Supplier<DATA>)()->{
            return otherwiseSupplier.get();
        });
    }
    
    public default Result<DATA> otherwiseGet(FailableFunc0<DATA> otherwiseSupplier) {
        DATA value = getValue();
        if (value != null)
            return this;
        
        return ImmutableResult.from((FailableFunc0<DATA>)()->{
            return otherwiseSupplier.get();
        });
    }
    
    public default DATA orElse(DATA elseValue) {
        DATA value = getValue();
        if (value == null)
            return elseValue;
        return value;
    }
    
    public default DATA orElseGet(Supplier<? extends DATA> elseSupplier) {
        DATA value = getValue();
        if (value == null)
            return elseSupplier.get();
        return value;
    }
    
    public default MayBe<MayBe<DATA>> asMayBe() {
        val exception = getException();
        if (exception != null)
            return MayBe.nothing();
        DATA value = getValue();
        return MayBe.of(MayBe.of(value));
    }
    
    public default MayBe<DATA> getMayBeValue() {
        DATA value = getValue();
        return MayBe.of(value);
    }
    
    public default MayBe<Exception> getMayBeException() {
        val exception = getException();
        return MayBe.of(exception);
    }
    
    public default DATA orThrow() throws Exception {
        val exception = getException();
        if (exception != null)
            throw exception;
        DATA value = getValue();
        return value;
    }
    public default DATA orThrowRuntimeException() {
        val exception = getException();
        if (exception == null) {
            DATA value = getValue();
            return value;
        }
        
        if (exception instanceof RuntimeException)
            throw (RuntimeException)exception;
        
        throw new RuntimeException(exception);
    }
    public default <THROWABLE extends Exception> DATA orThrow(Function<Exception, THROWABLE> toException) 
            throws THROWABLE {
        val exception = getException();
        if (exception == null) {
            DATA value = getValue();
            return value;
        }
        throw toException.apply(exception);
    }
    public default <RUNTIMEEXCEPTION extends RuntimeException> 
            DATA orThrowRuntimeException(Function<Exception, RUNTIMEEXCEPTION> toRuntimeException) {
        val exception = getException();
        if (exception == null) {
            DATA value = getValue();
            return value;
        }
        throw toRuntimeException.apply(exception);
    }

    @Override
    public default <TARGET> Monad<Result<?>, TARGET> _of(TARGET target) {
        return ImmutableResult.of(target);
    }
    
    public default Optional<DATA> toOptional() {
        return Optional.ofNullable(this.get());
    }
}
