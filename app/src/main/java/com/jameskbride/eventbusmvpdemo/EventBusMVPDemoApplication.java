package com.jameskbride.eventbusmvpdemo;

import android.app.Application;
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponent;
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponentFactory;

public class EventBusMVPDemoApplication extends Application {
    ApplicationComponentFactory applicationComponentFactory = new ApplicationComponentFactory();
    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = applicationComponentFactory.build();
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}