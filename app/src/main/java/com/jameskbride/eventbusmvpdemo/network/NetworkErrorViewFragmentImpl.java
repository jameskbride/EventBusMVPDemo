package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewPresenter.NetworkErrorView;

import javax.inject.Inject;

import static com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.NETWORK_REQUEST;

public class NetworkErrorViewFragmentImpl implements NetworkErrorView{

    private NetworkErrorViewPresenter presenter;
    private NetworkErrorViewFragment networkErrorViewFragment;

    @Inject
    public NetworkErrorViewFragmentImpl(NetworkErrorViewPresenter presenter) {
        this.presenter = presenter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, final NetworkErrorViewFragment networkErrorViewFragment) {
        this.networkErrorViewFragment = networkErrorViewFragment;
        View view = inflater.inflate(R.layout.network_error, container);
        presenter.view = this;

        view.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NetworkRequestEvent networkRequestEvent = (NetworkRequestEvent) networkErrorViewFragment.getArguments().getSerializable(NETWORK_REQUEST);
                presenter.retry(networkRequestEvent);
            }
        });

        return view;
    }

    public void onResume(NetworkErrorViewFragment networkErrorViewFragment) {
    }

    public void onPause(NetworkErrorViewFragment networkErrorViewFragment) {
    }

    @Override
    public void dismiss() {
        networkErrorViewFragment.dismiss();
    }
}
