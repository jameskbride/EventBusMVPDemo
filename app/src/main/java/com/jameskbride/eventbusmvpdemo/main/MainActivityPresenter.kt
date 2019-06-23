package com.jameskbride.eventbusmvpdemo.main

import androidx.annotation.StringRes
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.ProfileApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(val profileApi: ProfileApi) {
    lateinit var view: MainActivityView

    fun getProfile(id: String) {
        val call: Call<ProfileResponse> = profileApi.getProfile(id)
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
}

interface MainActivityView {
    fun displayError(@StringRes message: Int)
    fun displayProfileDetails(profileResponse: ProfileResponse)
    fun displayOrders(orderHistory: List<Order>)
    fun displayNoOrders()
}