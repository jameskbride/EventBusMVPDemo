package com.jameskbride.eventbusmvpdemo.network.service;

import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.bus.BusAware;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BurritosToGoService extends BusAware {
    private final BurritosToGoApi burritosToGoApi;

    @Inject
    public BurritosToGoService(EventBus bus, BurritosToGoApi burritosToGoApi) {
        super(bus);
        this.burritosToGoApi = burritosToGoApi;
    }

    @Subscribe
    public void onGetProfileEvent(GetProfileEvent getProfileEvent) {
        Call<ProfileResponse> call = burritosToGoApi.getProfile(getProfileEvent.getId());
        call.enqueue(new Callback<ProfileResponse>() {
            @Override public void onFailure(Call<ProfileResponse> call, Throwable throwable) {
                bus.post(new GetProfileErrorEvent());
            }

            @Override public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                bus.post(new GetProfileResponseEvent(response.body()));
            }
        });
    }
}
