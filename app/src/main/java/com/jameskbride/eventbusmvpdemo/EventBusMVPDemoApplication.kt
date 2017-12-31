package com.jameskbride.eventbusmvpdemo

import android.app.Application
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponent
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponentFactory

class EventBusMVPDemoApplication constructor(
        val applicationComponentFactory:ApplicationComponentFactory = ApplicationComponentFactory()): Application() {

    override fun onCreate() {
        super.onCreate()

        applicationComponent = applicationComponentFactory.build()

    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}