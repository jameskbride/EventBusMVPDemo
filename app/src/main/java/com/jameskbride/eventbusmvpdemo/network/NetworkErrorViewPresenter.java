package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.BusAware;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.greenrobot.eventbus.EventBus;

public class NetworkErrorViewPresenter extends BusAware {

    NetworkErrorView view;

    public NetworkErrorViewPresenter(EventBus bus) {
        super(bus);
    }

    public void retry(NetworkRequestEvent networkRequestEvent) {
        bus.post(networkRequestEvent);
        view.dismiss();
    }

    public interface NetworkErrorView {
        void dismiss();
    }
}
