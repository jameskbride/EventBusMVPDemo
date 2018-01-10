package com.jameskbride.eventbusmvpdemo.injection;

import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication;
import com.jameskbride.eventbusmvpdemo.main.MainActivity;
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment;
import com.jameskbride.eventbusmvpdemo.security.SecurityErrorViewFragment;

import dagger.Component;
import javax.inject.Singleton;


@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(MainActivity mainActivity);
    void inject(EventBusMVPDemoApplication eventBusMVPDemoApplication);
    void inject(NetworkErrorViewFragment networkErrorViewFragment);
    void inject(SecurityErrorViewFragment securityErrorViewFragment);
}