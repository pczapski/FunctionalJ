package functionalj.types.promise;

import static java.util.Objects.requireNonNull;

import java.util.concurrent.TimeUnit;

import functionalj.types.result.Result;

@SuppressWarnings("javadoc")
public abstract class AbstractDeferAction<DATA> implements HasPromise<DATA> {
    
    protected final Promise<DATA> promise;
    
    AbstractDeferAction(Promise<DATA> promise) {
        this.promise = requireNonNull(promise);
    }
    
    public final Promise<DATA> getPromise() {
        return promise;
    }
    public final Result<DATA> getCurentResult() {
        return promise.getCurrentResult();
    }
    public final Result<DATA> getResult() {
        return promise.getResult();
    }
    public final Result<DATA> getResult(long timeout, TimeUnit unit) {
        return promise.getResult(timeout, unit);
    }
    
}