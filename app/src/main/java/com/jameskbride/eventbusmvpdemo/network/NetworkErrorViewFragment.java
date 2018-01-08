package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication;

import javax.inject.Inject;

public class NetworkErrorViewFragment extends DialogFragment {

    @Inject
    NetworkErrorViewFragmentImpl delegate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((EventBusMVPDemoApplication)getActivity().getApplication()).getApplicationComponent().inject(this);

        return delegate.onCreateView(inflater, container, savedInstanceState, this);
    }
}
