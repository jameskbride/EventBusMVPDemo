package com.jameskbride.eventbusmvpdemo;

import retrofit2.Callback;
import retrofit2.Response;

public class SuccessCallFake<T> extends BaseCallFake<T> {

    private Response<T> response;

    public SuccessCallFake(Response<T> response) {

        this.response = response;
    }

    @Override public void enqueue (Callback<T> callback) {
        callback.onResponse(this, response);
    }
}