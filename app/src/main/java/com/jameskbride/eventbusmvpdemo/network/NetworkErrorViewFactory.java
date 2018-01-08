package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;

import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import static com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.NETWORK_REQUEST;

public class NetworkErrorViewFactory {

    public NetworkErrorViewFragment make(NetworkRequestEvent networkRequestEvent) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NETWORK_REQUEST, networkRequestEvent);
        NetworkErrorViewFragment networkErrorViewFragment = new NetworkErrorViewFragment();
        networkErrorViewFragment.setArguments(bundle);

        return networkErrorViewFragment;
    }
}
