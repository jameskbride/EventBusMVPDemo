package com.jameskbride.eventbusmvpdemo.bus

import org.greenrobot.eventbus.EventBus

interface BusAware {
    val eventBus: EventBus
    fun open() {
        eventBus.register(this)
    }

    fun close() {
        eventBus.unregister(this)
    }
}