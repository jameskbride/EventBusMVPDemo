package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import javax.inject.Inject;

import static com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.NETWORK_REQUEST;

public class NetworkErrorViewFragmentImpl {

    private NetworkErrorViewPresenter presenter;

    @Inject
    public NetworkErrorViewFragmentImpl(NetworkErrorViewPresenter presenter) {
        this.presenter = presenter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, final NetworkErrorViewFragment networkErrorViewFragment) {
        View view = inflater.inflate(R.layout.network_error, container);

        view.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkRequestEvent networkRequestEvent = (NetworkRequestEvent) networkErrorViewFragment.getArguments().getSerializable(NETWORK_REQUEST);
                presenter.retry(networkRequestEvent);
            }
        });

        return view;
    }
}
