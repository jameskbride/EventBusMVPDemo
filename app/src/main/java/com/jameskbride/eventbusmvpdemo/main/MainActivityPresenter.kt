package com.jameskbride.eventbusmvpdemo.main

import android.support.annotation.StringRes
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainActivityPresenter @Inject constructor(val burritosToGoApi: BurritosToGoApi) {
    lateinit var view: MainActivityView

    fun getProfile(id: String) {
        val call: Call<ProfileResponse> = burritosToGoApi.getProfile(id)
        call.enqueue(object: Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>?, t: Throwable?) {
                view.displayError(R.string.oops)
            }

            override fun onResponse(call: Call<ProfileResponse>?, response: Response<ProfileResponse>?) {
                view.displayProfileDetails(response?.body()!!)
            }

        })
    }
}

interface MainActivityView {
    fun displayError(@StringRes message: Int)
    fun displayProfileDetails(profileResponse: ProfileResponse)
}