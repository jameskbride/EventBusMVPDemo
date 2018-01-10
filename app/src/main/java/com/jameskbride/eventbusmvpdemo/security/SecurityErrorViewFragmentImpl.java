package com.jameskbride.eventbusmvpdemo.security;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.security.SecurityErrorViewPresenter.SecurityErrorView;

import javax.inject.Inject;

public class SecurityErrorViewFragmentImpl implements SecurityErrorView{

    private SecurityErrorViewPresenter presenter;
    private SecurityErrorViewFragment networkErrorViewFragment;

    @Inject
    public SecurityErrorViewFragmentImpl(SecurityErrorViewPresenter presenter) {
        this.presenter = presenter;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, final SecurityErrorViewFragment securityErrorViewFragment) {
        this.networkErrorViewFragment = securityErrorViewFragment;
        View view = inflater.inflate(R.layout.security_error, container);
        presenter.view = this;

        view.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.dismiss();
            }
        });

        return view;
    }

    public void onResume(SecurityErrorViewFragment securityErrorViewFragment) {
    }

    public void onPause(SecurityErrorViewFragment securityErrorViewFragment) {
    }

    @Override
    public void dismiss() {
        networkErrorViewFragment.dismiss();
    }
}
