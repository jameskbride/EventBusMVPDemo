package com.jameskbride.eventbusmvpdemo.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import javax.inject.Inject;

public class MainActivityImpl {

    private BurritosToGoApi burritosToGoApi;

    ToasterWrapper toasterWrapper = new ToasterWrapper();
    OrdersAdapterFactory ordersAdapterFactory = new OrdersAdapterFactory();

    @Inject
    public MainActivityImpl(BurritosToGoApi burritosToGoApi) {
        this.burritosToGoApi = burritosToGoApi;
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

            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                setProfileDetails(mainActivity, response);

                showOrders(response.body().getOrderHistory(), mainActivity);
            }
        });
    }

    private void setProfileDetails(MainActivity mainActivity, Response<ProfileResponse> response) {
        TextView customerName = mainActivity.findViewById(R.id.customer_name);
        customerName.setText(response.body().getFirstName() +  " " + response.body().getLastName());

        TextView addressLine1 = mainActivity.findViewById(R.id.address_line_1);
        addressLine1.setText(response.body().getAddressLine1());

        TextView addressLine2 = mainActivity.findViewById(R.id.address_line_2);
        addressLine2.setText(response.body().getAddressLine2());

        TextView city = mainActivity.findViewById(R.id.city);
        city.setText(response.body().getCity());

        TextView state = mainActivity.findViewById(R.id.state);
        state.setText(response.body().getState());

        TextView zipCode = mainActivity.findViewById(R.id.zipcode);
        zipCode.setText(response.body().getZipCode());
    }

    private void showOrders(List<Order> orderHistory, MainActivity mainActivity) {
        LinearLayout foundOrdersBlock = mainActivity.findViewById(R.id.found_orders_block);
        LinearLayout noOrdersBlock = mainActivity.findViewById(R.id.no_orders_block);
        if (!orderHistory.isEmpty()) {
            foundOrdersBlock.setVisibility(View.VISIBLE);
            noOrdersBlock.setVisibility(View.GONE);

            List<String> descriptions = new ArrayList<>();
            for (Order order : orderHistory) {
                descriptions.add(order.getDescription());
            }

            ArrayAdapter<String> adapter = ordersAdapterFactory.make(mainActivity, android.R.layout.simple_list_item_1, descriptions);
            ListView ordersList = mainActivity.findViewById(R.id.order_list);
            ordersList.setAdapter(adapter);
        } else {
            noOrdersBlock.setVisibility(View.VISIBLE);
            foundOrdersBlock.setVisibility(View.GONE);
        }
    }
}