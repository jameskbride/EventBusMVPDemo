package com.jameskbride.eventbusmvpdemo.main

import androidx.annotation.StringRes
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(override val eventBus: EventBus) : BusAware {
    lateinit var view: MainActivityView

    fun getProfile(id: String) {
        eventBus.post(GetProfileEvent(id))
    }

    @Subscribe
    fun onGetProfileResponseEvent(getProfileResponseEvent: GetProfileResponseEvent) {
        view.displayProfileDetails(getProfileResponseEvent.profileResponse)
        displayOrders(getProfileResponseEvent.profileResponse.orderHistory)
    }

    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent) {
        view.displayError(R.string.oops)
    }

    private fun displayOrders(orderHistory: List<Order>) {
        if (orderHistory.isNotEmpty()) {
            view.displayOrders(orderHistory)
        } else {
            view.displayNoOrders()
        }
    }
}

interface MainActivityView {
    fun displayError(@StringRes message: Int)
    fun displayProfileDetails(profileResponse: ProfileResponse)
    fun displayOrders(orderHistory: List<Order>)
    fun displayNoOrders()
}