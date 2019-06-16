package com.jameskbride.eventbusmvpdemo.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.Companion.NETWORK_REQUEST
import javax.inject.Inject


class NetworkErrorViewFragmentImpl @Inject constructor(val presenter: NetworkErrorViewPresenter) : NetworkErrorView {
    lateinit var networkErrorViewFragment: NetworkErrorViewFragment

    fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?, networkErrorViewFragment: NetworkErrorViewFragment): View {
        this.networkErrorViewFragment = networkErrorViewFragment
        val view = inflater!!.inflate(R.layout.network_error, container)
        presenter.view = this

        view.findViewById<Button>(R.id.retry_button).setOnClickListener {view: View? ->
            val networkRequestEvent = networkErrorViewFragment.arguments?.getSerializable(NETWORK_REQUEST) as NetworkRequestEvent
            presenter.retry(networkRequestEvent)
        }

        return view
    }

    fun onResume(networkErrorViewFragment: NetworkErrorViewFragment) {}

    fun onPause(networkErrorViewFragment: NetworkErrorViewFragment) {}

    override fun dismiss() {
        networkErrorViewFragment.dismiss()
    }
}