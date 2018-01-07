package com.jameskbride.eventbusmvpdemo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    @Inject
    MainActivityImpl delegate;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((EventBusMVPDemoApplication)getApplication()).getApplicationComponent().inject(this);
        delegate.onCreate(savedInstanceState, this);
    }

    @Override public void onResume() {
        super.onResume();
        delegate.onResume(this);
    }
}
