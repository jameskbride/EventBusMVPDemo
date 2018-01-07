package com.jameskbride.eventbusmvpdemo;

import android.app.Application;
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponent;
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponentFactory;
import com.jameskbride.eventbusmvpdemo.network.service.BurritosToGoService;

import javax.inject.Inject;

public class EventBusMVPDemoApplication extends Application {
    ApplicationComponentFactory applicationComponentFactory = new ApplicationComponentFactory();
    private ApplicationComponent applicationComponent;

    @Inject
    BurritosToGoService burritosToGoService;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = applicationComponentFactory.build();
        applicationComponent.inject(this);
        burritosToGoService.open();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}