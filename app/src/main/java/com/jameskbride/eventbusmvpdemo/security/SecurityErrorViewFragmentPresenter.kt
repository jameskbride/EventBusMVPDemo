package com.jameskbride.eventbusmvpdemo.security

import org.greenrobot.eventbus.EventBus
import com.jameskbride.eventbusmvpdemo.bus.BusAware


class SecurityErrorViewPresenter(override val eventBus: EventBus) : BusAware {

    internal var view: SecurityErrorView? = null

    fun dismiss() {
        view!!.dismiss()
    }

    interface SecurityErrorView {
        fun dismiss()
    }
}