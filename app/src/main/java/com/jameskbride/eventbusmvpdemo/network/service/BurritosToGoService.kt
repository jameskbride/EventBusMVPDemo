package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.NetworkApiWrapper
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
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
                    object: Consumer<ProfileResponse> {
                        override fun accept(result: ProfileResponse) {
                            eventBus.post(GetProfileResponseEvent(result))
                        }
                    },
                    NetworkApiWrapper(eventBus, { error -> eventBus.post(GetProfileErrorEvent()) }, getProfileEvent)
            )
    }
}