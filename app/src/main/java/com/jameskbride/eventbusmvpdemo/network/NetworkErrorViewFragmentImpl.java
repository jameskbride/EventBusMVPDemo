package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.R;

public class NetworkErrorViewFragmentImpl {
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, NetworkErrorViewFragment networkErrorViewFragment) {
        return inflater.inflate(R.layout.network_error, container);
    }
}
