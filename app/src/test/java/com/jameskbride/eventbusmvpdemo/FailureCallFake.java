package com.jameskbride.eventbusmvpdemo;

public class FailureCallFake<T> extends BaseCallFake<T> {
    private Throwable throwable;

    public FailureCallFake(Throwable throwable) {

        this.throwable = throwable;
    }

    @Override public void enqueue (retrofit2.Callback<T> callback) {
        callback.onFailure(this, throwable);
    }
}
