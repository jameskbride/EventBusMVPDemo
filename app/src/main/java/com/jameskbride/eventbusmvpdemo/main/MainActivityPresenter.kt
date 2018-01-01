package com.jameskbride.eventbusmvpdemo.main

import android.support.annotation.StringRes
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(val burritosToGoApi: BurritosToGoApi, val eventBus: EventBus) {
    lateinit var view: MainActivityView

    fun getProfile(id: String) {
        val call: Call<ProfileResponse> = burritosToGoApi.getProfile(id)
        call.enqueue(object: Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>?, t: Throwable?) {
                view.displayError(R.string.oops)
            }

            override fun onResponse(call: Call<ProfileResponse>?, response: Response<ProfileResponse>?) {
                val profileResponse = response?.body()!!
                view.displayProfileDetails(profileResponse)
                val orderHistory = profileResponse.orderHistory
                displayOrders(orderHistory)
            }
        })
    }

    private fun displayOrders(orderHistory: List<Order>) {
        if (orderHistory.isNotEmpty()) {
            view.displayOrders(orderHistory)
        } else {
            view.displayNoOrders()
        }
    }

    @Subscribe
    fun onGetProfileResponseEvent(getProfileResponseEvent: GetProfileResponseEvent) {

    }

    fun open() {
        eventBus.register(this)
    }

    fun close() {
        eventBus.unregister(this)
    }
}

interface MainActivityView {
    fun displayError(@StringRes message: Int)
    fun displayProfileDetails(profileResponse: ProfileResponse)
    fun displayOrders(orderHistory: List<Order>)
    fun displayNoOrders()
}