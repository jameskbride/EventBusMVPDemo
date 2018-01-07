package com.jameskbride.eventbusmvpdemo.main;

import android.support.annotation.StringRes;

import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.BusAware;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityPresenter extends BusAware {

    MainActivityView view;

    @Inject
    public MainActivityPresenter(EventBus bus) {
        super(bus);
    }

    public void getProfile(String id) {
        bus.post(new GetProfileEvent(id));
    }

    @Subscribe
    public void onGetProfileResponseEvent(GetProfileResponseEvent getProfileResponseEvent) {
        ProfileResponse profileResponse = getProfileResponseEvent.getProfileResponse();
        view.displayProfileDetails(profileResponse);
        List<Order> orderHistory = profileResponse.getOrderHistory();
        displayOrders(orderHistory);
    }

    @Subscribe
    public void onGetProfileErrorEvent(GetProfileErrorEvent getProfileErrorEvent) {
        view.displayError(R.string.oops);
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
