package com.jameskbride.eventbusmvpdemo.main;

import android.support.annotation.StringRes;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter {

    MainActivityView view;
    private BurritosToGoApi burritosToGoApi;

    @Inject
        public MainActivityPresenter(BurritosToGoApi burritosToGoApi) {
        this.burritosToGoApi = burritosToGoApi;
    }

    public void getProfile(String id) {
        Call<ProfileResponse> call = burritosToGoApi.getProfile(id);
        call.enqueue(new  Callback<ProfileResponse>() {
            @Override public void onFailure(Call<ProfileResponse> call, Throwable t) {
                view.displayError(R.string.oops);
            }

            @Override public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                ProfileResponse profileResponse = response.body();
                view.displayProfileDetails(profileResponse);
                List<Order> orderHistory = profileResponse.getOrderHistory();
                displayOrders(orderHistory);
            }
        });
    }

    private void displayOrders(List<Order> orderHistory) {
        if (!orderHistory.isEmpty()) {
            view.displayOrders(orderHistory);
        } else {
            view.displayNoOrders();
        }
    }

    public void setView(MainActivityView view) {
        this.view = view;
    }
}

interface MainActivityView {
    void displayError(@StringRes int message);
    void displayProfileDetails(ProfileResponse profileResponse);
    void displayOrders(List<Order> orderHistory);
    void displayNoOrders();
}
