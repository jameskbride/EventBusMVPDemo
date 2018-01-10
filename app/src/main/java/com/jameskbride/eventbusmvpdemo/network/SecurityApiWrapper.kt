package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.bus.SecurityErrorEvent
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import retrofit2.HttpException

class SecurityApiWrapper<T : Throwable>(
        override val eventBus: EventBus,
        val consumer: Consumer<T>): Consumer<T>, BusAware {

    override fun accept(t: T) {
        if (t is HttpException) {
            val exception = t as HttpException
            if (401 == exception.code()) {
                eventBus.post(SecurityErrorEvent())
            } else {
                consumer.accept(t)
            }
        } else {
            consumer.accept(t)
        }
    }
}