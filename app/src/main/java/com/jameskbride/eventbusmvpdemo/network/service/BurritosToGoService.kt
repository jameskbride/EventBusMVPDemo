package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class BurritosToGoService @Inject constructor(
        override val eventBus: EventBus,
        val burritosToGoApi: BurritosToGoApi,
        val processScheduler: Scheduler,
        val androidScheduler: Scheduler): BusAware {

    @Subscribe
    fun onGetProfileEvent(getProfileEvent: GetProfileEvent) {
        val call: Observable<ProfileResponse> = burritosToGoApi.getProfile(getProfileEvent.id)
        call
            .subscribeOn(processScheduler)
            .observeOn(androidScheduler)
            .subscribe (
                    { result ->
                        eventBus.post(GetProfileResponseEvent(result))
                    },
                    {error ->
                        eventBus.post(GetProfileErrorEvent())
                    }
            )
    }
}