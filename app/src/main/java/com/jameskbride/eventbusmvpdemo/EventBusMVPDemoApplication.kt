package com.jameskbride.eventbusmvpdemo

import android.app.Application
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponent
import com.jameskbride.eventbusmvpdemo.injection.ApplicationComponentFactory
import com.jameskbride.eventbusmvpdemo.network.service.ProfileService
import javax.inject.Inject

class EventBusMVPDemoApplication constructor(
        val applicationComponentFactory:ApplicationComponentFactory = ApplicationComponentFactory()): Application() {

    @Inject
    lateinit var burritosToGoService:ProfileService

    override fun onCreate() {
        super.onCreate()

        applicationComponent = applicationComponentFactory.build()
        applicationComponent.inject(this)

        burritosToGoService.open()

    }

    companion object {
        lateinit var applicationComponent: ApplicationComponent
    }
}