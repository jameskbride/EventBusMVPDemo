package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;

import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

public class NetworkErrorViewFactory {
    private static final String NETWORK_REQUEST = "NETWORK_REQUEST";

    public NetworkErrorViewFragment make(NetworkRequestEvent networkRequestEvent) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NETWORK_REQUEST, networkRequestEvent);
        NetworkErrorViewFragment networkErrorViewFragment = new NetworkErrorViewFragment();
        networkErrorViewFragment.setArguments(bundle);

        return networkErrorViewFragment;
    }
}
