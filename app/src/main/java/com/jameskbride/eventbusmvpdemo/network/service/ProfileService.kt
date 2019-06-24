package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.bus.BusAware
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.ProfileApi
import com.jameskbride.eventbusmvpdemo.network.NetworkApiWrapper
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.jameskbride.eventbusmvpdemo.network.SecurityApiWrapper
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

class ProfileService @Inject constructor(
        override val eventBus: EventBus,
        val profileApi: ProfileApi,
        val processScheduler: Scheduler,
        val androidScheduler: Scheduler): BusAware {

    @Subscribe
    fun onGetProfileEvent(getProfileEvent: GetProfileEvent) {
        val call: Observable<ProfileResponse> = profileApi.getProfile(getProfileEvent.id)
        val getProfileConsumer = Consumer<ProfileResponse> { result -> eventBus.post(GetProfileResponseEvent(result)) }
        val getProfileErrorConsumer = Consumer<Throwable>{ error: Throwable -> eventBus.post(GetProfileErrorEvent()) }
        val securityApiWrapper = SecurityApiWrapper(eventBus, getProfileErrorConsumer)
        val networkApiWrapper = NetworkApiWrapper(eventBus, securityApiWrapper, getProfileEvent)

        call
            .subscribeOn(processScheduler)
            .observeOn(androidScheduler)
            .subscribe (
                    getProfileConsumer,
                    networkApiWrapper
            )
    }
}