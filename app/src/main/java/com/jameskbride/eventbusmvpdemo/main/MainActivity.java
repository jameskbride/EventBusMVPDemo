package com.jameskbride.eventbusmvpdemo.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.jameskbride.eventbusmvpdemo.EventBusMVPDemoApplication;
import javax.inject.Inject;


class MainActivity extends AppCompatActivity {

    @Inject
    MainActivityImpl delegate;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((EventBusMVPDemoApplication)getApplication()).getApplicationComponent().inject(this);
        delegate.onCreate(savedInstanceState, this);
    }

    public void onResume() {
        super.onResume();
        delegate.onResume(this);
    }
}
