package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.SecurityErrorEvent;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

public class SecurityApiWrapper<T extends Throwable> implements Consumer<T> {

    private EventBus eventBus;
    private Consumer consumer;

    public SecurityApiWrapper(EventBus eventBus, Consumer consumer) {
        this.eventBus = eventBus;
        this.consumer = consumer;
    }

    @Override
    public void accept(T t) throws Exception {
        if (t instanceof HttpException) {
            HttpException exception = (HttpException)t;
            if (401 == exception.code()) {
                eventBus.post(new SecurityErrorEvent());
            } else {
                consumer.accept(t);
            }
        } else {
            consumer.accept(t);
        }
    }
}