package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BurritosToGoService @Inject constructor(val eventBus: EventBus, val burritosToGoApi: BurritosToGoApi) {

    fun onGetProfileEvent(getProfileEvent: GetProfileEvent) {
        val call: Call<ProfileResponse> = burritosToGoApi.getProfile(getProfileEvent.id)
        call.enqueue(object:Callback<ProfileResponse> {
            override fun onFailure(call: Call<ProfileResponse>?, t: Throwable?) {
                eventBus.post(GetProfileErrorEvent())
            }

            override fun onResponse(call: Call<ProfileResponse>?, response: Response<ProfileResponse>?) {
                eventBus.post(GetProfileResponseEvent(response!!.body()!!))
            }
        })
    }
}