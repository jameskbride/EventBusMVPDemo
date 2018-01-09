package com.jameskbride.eventbusmvpdemo.network

import android.os.Bundle
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.Companion.NETWORK_REQUEST


class NetworkErrorViewFactory {

    fun make(networkRequestEvent: NetworkRequestEvent): NetworkErrorViewFragment {
        val bundle = Bundle()
        bundle.putSerializable(NETWORK_REQUEST, networkRequestEvent)
        val networkErrorViewFragment = NetworkErrorViewFragment()
        networkErrorViewFragment.arguments = bundle

        return networkErrorViewFragment
    }
}