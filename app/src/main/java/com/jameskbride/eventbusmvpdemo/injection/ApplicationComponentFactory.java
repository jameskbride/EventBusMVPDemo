package com.jameskbride.eventbusmvpdemo.injection;

public class ApplicationComponentFactory {
    public ApplicationComponent build() {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(new ApplicationModule())
                .build();
    }
}
