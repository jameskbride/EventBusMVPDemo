package com.jameskbride.eventbusmvpdemo.security;

import com.jameskbride.eventbusmvpdemo.bus.BusAware;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.greenrobot.eventbus.EventBus;

public class SecurityErrorViewPresenter extends BusAware {

    SecurityErrorView view;

    public SecurityErrorViewPresenter(EventBus bus) {
        super(bus);
    }

    public void dismiss() {
        view.dismiss();
    }

    public interface SecurityErrorView {
        void dismiss();
    }
}
