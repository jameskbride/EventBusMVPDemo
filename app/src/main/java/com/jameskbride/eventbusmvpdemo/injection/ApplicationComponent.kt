package com.jameskbride.eventbusmvpdemo.injection

import com.jameskbride.eventbusmvpdemo.main.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
}

class ApplicationComponentFactory {
    fun build(): ApplicationComponent {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule())
                .build()
    }
}