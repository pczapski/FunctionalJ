package functionalj.types.promise;

import java.util.Objects;

import lombok.val;

@SuppressWarnings("javadoc")
public class Subscription<DATA> implements HasPromise<DATA> {
    
    public static enum SubscriptionStatus {
        AWAITING, UNSUBSCRIBED, CANCELLED, COMPLETED;
        
        public boolean isAwaiting() {
            return AWAITING.equals(this);
        }
        public boolean isUnsubscribed() {
            return UNSUBSCRIBED.equals(this);
        }
        public boolean isCancelled() {
            return CANCELLED.equals(this);
        }
        public boolean isCompleted() {
            return COMPLETED.equals(this);
        }
        public boolean isNotDone() {
            return !isDone();
        }
        public boolean isDone() {
            return isCancelled() || isCompleted();
        }
    }
    
    private final Promise<DATA> promise;
    
    Subscription(Promise<DATA> promise) {
        this.promise = Objects.requireNonNull(promise);
    }
    
    public Promise<DATA> getPromise() {
        return promise;
    }
    
    public SubscriptionStatus getSubscriptionStatus() {
        val promiseStatus = promise.getStatus();
        Objects.requireNonNull(promiseStatus);
        
        if (promiseStatus.isComplete())
            return SubscriptionStatus.COMPLETED;
        
        if (promiseStatus.isAborted())
            return SubscriptionStatus.CANCELLED;
        
        if (!promise.isSubscribed(this))
            return SubscriptionStatus.UNSUBSCRIBED;
        
        return SubscriptionStatus.AWAITING;
    }
    public Subscription<DATA> unsubscribe() {
        promise.unsubscribe(this);
        return this;
    }
    
    public boolean isAwaiting() {
        return getSubscriptionStatus().isAwaiting();
    }
    public boolean isUnsubscribed() {
        return getSubscriptionStatus().isUnsubscribed();
    }
    public boolean isCancelled() {
        return getSubscriptionStatus().isCancelled();
    }
    public boolean isCompleted() {
        return getSubscriptionStatus().isCompleted();
    }
    public boolean isNotDone() {
        return getSubscriptionStatus().isNotDone();
    }
    public boolean isDone() {
        return getSubscriptionStatus().isDone();
    }
    
}