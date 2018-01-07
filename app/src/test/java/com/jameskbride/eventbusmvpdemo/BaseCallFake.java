package com.jameskbride.eventbusmvpdemo;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaseCallFake<T> implements Call<T> {
    @Override public void enqueue(Callback<T> callback) {
    }

    @Override public boolean isCanceled() {
        return false;
    }

    @Override public Request request() {
        return null;
    }

    @Override public Call<T> clone() {
        return null;
    }

    @Override public void cancel() {
    }

    @Override public Response<T> execute() {
        return null;
    }

    @Override public boolean isExecuted() {
        return false;
    }
}