package com.jameskbride.eventbusmvpdemo.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var delegate:MainActivityImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBusMVPDemoApplication.applicationComponent.inject(this)
        delegate.onCreate(savedInstanceState, this)
    }

    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }
}
