package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import java.io.IOException


class NetworkApiWrapper<T : Throwable>(
        override val eventBus: EventBus,
        val consumer: (t: T) -> Unit,
        val networkRequestEvent: NetworkRequestEvent) : Consumer<T>, BusAware {

    override fun accept(t: T) {
        if (t is IOException) {
            eventBus.post(NetworkErrorEvent(networkRequestEvent))
        } else {
            consumer(t)
        }
    }
}