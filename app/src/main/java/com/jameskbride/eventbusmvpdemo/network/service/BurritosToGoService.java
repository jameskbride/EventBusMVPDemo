package com.jameskbride.eventbusmvpdemo.network.service;

import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.bus.BusAware;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.NetworkApiWrapper;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;

public class BurritosToGoService extends BusAware {
    private final BurritosToGoApi burritosToGoApi;
    private final Scheduler processScheduler;
    private final Scheduler androidScheduler;

    @Inject
    public BurritosToGoService(EventBus bus, BurritosToGoApi burritosToGoApi, Scheduler processScheduler, Scheduler androidScheduler) {
        super(bus);
        this.burritosToGoApi = burritosToGoApi;
        this.processScheduler = processScheduler;
        this.androidScheduler = androidScheduler;
    }

    @Subscribe
    public void onGetProfileEvent(GetProfileEvent getProfileEvent) {
        Observable<ProfileResponse> call = burritosToGoApi.getProfile(getProfileEvent.getId());
        call
            .subscribeOn(processScheduler)
            .observeOn(androidScheduler)
            .subscribe(
                    new Consumer<ProfileResponse>() {
                           @Override
                           public void accept(ProfileResponse response) throws Exception {
                               bus.post(new GetProfileResponseEvent(response));
                           }
                       },
                    new NetworkApiWrapper(bus, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            bus.post(new GetProfileErrorEvent());
                        }
                    }, getProfileEvent)
            );
    }
}
