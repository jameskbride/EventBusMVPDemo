package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import org.greenrobot.eventbus.EventBus
import com.jameskbride.eventbusmvpdemo.bus.BusAware


class NetworkErrorViewPresenter constructor(override val eventBus: EventBus) : BusAware {

    lateinit var view: NetworkErrorView

    fun retry(networkRequestEvent: NetworkRequestEvent) {
        eventBus.post(networkRequestEvent)
        view.dismiss()
    }
}

interface NetworkErrorView {
    fun dismiss()
}