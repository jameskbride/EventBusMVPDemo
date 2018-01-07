package com.jameskbride.eventbusmvpdemo.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class MainActivityImpl implements MainActivityView {

    ToasterWrapper toasterWrapper = new ToasterWrapper();
    OrdersAdapterFactory ordersAdapterFactory = new OrdersAdapterFactory();
    private MainActivityPresenter presenter;
    private MainActivity mainActivity;

    @Inject
    public MainActivityImpl(MainActivityPresenter presenter) {
        this.presenter = presenter;
    }

    public void onCreate(Bundle savedInstanceState, MainActivity mainActivity) {
        mainActivity.setContentView(R.layout.activity_main);
        this.mainActivity = mainActivity;
        presenter.setView(this);
    }

    public void onResume(final MainActivity mainActivity) {
        presenter.open();
        presenter.getProfile("1");
    }

    public void onPause(final MainActivity mainActivity) {
        presenter.close();
    }

    @Override
    public void displayError(int message) {
        toasterWrapper.makeText(mainActivity, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void displayProfileDetails(ProfileResponse response) {
        TextView customerName = mainActivity.findViewById(R.id.customer_name);
        customerName.setText(response.getFirstName() +  " " + response.getLastName());

        TextView addressLine1 = mainActivity.findViewById(R.id.address_line_1);
        addressLine1.setText(response.getAddressLine1());

        TextView addressLine2 = mainActivity.findViewById(R.id.address_line_2);
        addressLine2.setText(response.getAddressLine2());

        TextView city = mainActivity.findViewById(R.id.city);
        city.setText(response.getCity());

        TextView state = mainActivity.findViewById(R.id.state);
        state.setText(response.getState());

        TextView zipCode = mainActivity.findViewById(R.id.zipcode);
        zipCode.setText(response.getZipCode());
    }

    @Override
    public void displayOrders(List<Order> orderHistory) {
        LinearLayout foundOrdersBlock = mainActivity.findViewById(R.id.found_orders_block);
        foundOrdersBlock.setVisibility(View.VISIBLE);

        LinearLayout noOrdersBlock = mainActivity.findViewById(R.id.no_orders_block);
        noOrdersBlock.setVisibility(View.GONE);

        List<String> descriptions = new ArrayList<>();
        for (Order order : orderHistory) {
            descriptions.add(order.getDescription());
        }

        ArrayAdapter<String> adapter = ordersAdapterFactory.make(mainActivity, android.R.layout.simple_list_item_1, descriptions);
        ListView ordersList = mainActivity.findViewById(R.id.order_list);
        ordersList.setAdapter(adapter);
    }

    @Override
    public void displayNoOrders() {
        LinearLayout foundOrdersBlock = mainActivity.findViewById(R.id.found_orders_block);
        foundOrdersBlock.setVisibility(View.GONE);

        LinearLayout noOrdersBlock = mainActivity.findViewById(R.id.no_orders_block);
        noOrdersBlock.setVisibility(View.VISIBLE);
    }
}