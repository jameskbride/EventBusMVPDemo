package com.jameskbride.eventbusmvpdemo.security

import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import javax.inject.Inject


class SecurityErrorViewFragment : DialogFragment() {

    @Inject
    lateinit var impl: SecurityErrorViewFragmentImpl

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        EventBusMVPDemoApplication.applicationComponent.inject(this)

        return impl.onCreateView(inflater, container!!, savedInstanceState!!, this)
    }

    override fun onResume() {
        super.onResume()
        impl.onResume(this)
    }

    override fun onPause() {
        super.onPause()

        impl!!.onPause(this)
    }
}