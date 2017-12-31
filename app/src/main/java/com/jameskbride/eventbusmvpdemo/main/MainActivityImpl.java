package com.jameskbride.eventbusmvpdemo.main;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivityImpl {

    private final BurritosToGoApi burritosToGoApi;
    private final ToasterWrapper toasterWrapper;

    @Inject
    public MainActivityImpl(BurritosToGoApi burritosToGoApi, ToasterWrapper toasterWrapper) {
        this.burritosToGoApi = burritosToGoApi;
        this.toasterWrapper = toasterWrapper;
    }

    public void onCreate(Bundle savedInstanceState, MainActivity mainActivity) {
        mainActivity.setContentView(R.layout.activity_main);
    }

    public void onResume(final MainActivity mainActivity) {
        final Call<ProfileResponse> call = burritosToGoApi.getProfile("1");

        call.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                toasterWrapper.makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG).show();
            }

            @Override public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                TextView name = mainActivity.findViewById(R.id.customer_name);
                name.setText(response.body().getFirstName() + response.body().getLastName());
            }
        });
    }


}