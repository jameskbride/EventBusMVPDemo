package com.jameskbride.eventbusmvpdemo.network

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication
import javax.inject.Inject


class NetworkErrorViewFragment : DialogFragment() {

    @Inject
    lateinit var impl: NetworkErrorViewFragmentImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        EventBusMVPDemoApplication.applicationComponent.inject(this)

        val view = impl.onCreateView(inflater, container, savedInstanceState, this)

        return view
    }

    override fun onResume() {
        super.onResume()
        impl.onResume(this)
    }

    override fun onPause() {
        super.onPause()

        impl.onPause(this)
    }

    companion object {
        val NETWORK_REQUEST = "NETWORK_REQUEST"
    }
}