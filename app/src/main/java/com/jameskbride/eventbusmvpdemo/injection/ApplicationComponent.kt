package com.jameskbride.eventbusmvpdemo.injection

import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication
import com.jameskbride.eventbusmvpdemo.main.MainActivity
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(application: EventBusMVPDemoApplication)
    fun inject(networkErrorViewFragment: NetworkErrorViewFragment)
}

class ApplicationComponentFactory {
    fun build(): ApplicationComponent {
        return DaggerApplicationComponent
                .builder()
                .applicationModule(ApplicationModule())
                .build()
    }
}