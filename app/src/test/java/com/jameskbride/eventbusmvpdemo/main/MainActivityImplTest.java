package com.jameskbride.eventbusmvpdemo.main;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jameskbride.eventbusmvpdemo.FailureCallFake;
import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.SuccessCallFake;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MainActivityImplTest {

    @Mock private MainActivity mainActivity;
    @Mock private BurritosToGoApi burritosToGoApi;
    @Mock private ToasterWrapper toasterWrapper;
    @Mock private TextView customerName;
    @Mock private TextView addressLine1;
    @Mock private TextView addressLine2;
    @Mock private TextView city;
    @Mock private TextView state;
    @Mock private TextView zipCode;
    @Mock private LinearLayout noOrdersBlock;
    @Mock private LinearLayout foundOrdersBlock;
    @Mock private ListView orders;
    @Mock private ArrayAdapter<String> ordersAdapter;
    @Mock private OrdersAdapterFactory ordersAdapterFactory;

    private MainActivityImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        subject = new MainActivityImpl(burritosToGoApi);
        subject.toasterWrapper = toasterWrapper;
        subject.ordersAdapterFactory = ordersAdapterFactory;

        when(mainActivity.findViewById(R.id.customer_name)).thenReturn(customerName);
        when(mainActivity.findViewById(R.id.address_line_1)).thenReturn(addressLine1);
        when(mainActivity.findViewById(R.id.address_line_2)).thenReturn(addressLine2);
        when(mainActivity.findViewById(R.id.city)).thenReturn(city);
        when(mainActivity.findViewById(R.id.state)).thenReturn(state);
        when(mainActivity.findViewById(R.id.zipcode)).thenReturn(zipCode);
        when(mainActivity.findViewById(R.id.no_orders_block)).thenReturn(noOrdersBlock);
        when(mainActivity.findViewById(R.id.found_orders_block)).thenReturn(foundOrdersBlock);
        when(mainActivity.findViewById(R.id.order_list)).thenReturn(orders);
    }

    @Test
    public void onCreateSetsTheContentView() {
        subject.onCreate(null, mainActivity);

        verify(mainActivity).setContentView(R.layout.activity_main);
    }

    @Test
    public void onResumeDisplaysAnErrorMessageOnGetProfileFailure() {
        FailureCallFake<ProfileResponse> profileResponseCall = new FailureCallFake(new IOException("parse exception"));

        when(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall);
        when(toasterWrapper.makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG)).thenReturn(toasterWrapper);

        subject.onResume(mainActivity);

        verify(toasterWrapper).makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG);
        verify(toasterWrapper).show();
    }

    @Test
    public void onResumeDisplaysTheProfileDetailsOnGetProfileResponse() {
        ProfileResponse profileResponse = buildProfileResponseWithoutOrders();
        SuccessCallFake<ProfileResponse> profileResponseCall = new SuccessCallFake(Response.success(profileResponse));

        when(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall);

        subject.onResume(mainActivity);

        verify(customerName).setText(profileResponse.getFirstName() + " " + profileResponse.getLastName());
        verify(addressLine1).setText(profileResponse.getAddressLine1());
        verify(addressLine2).setText(profileResponse.getAddressLine2());
        verify(city).setText(profileResponse.getCity());
        verify(state).setText(profileResponse.getState());
        verify(zipCode).setText(profileResponse.getZipCode());
    }

    @Test
    public void onResumeItDisplaysOrdersWhenTheyAreAvailable() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();
        SuccessCallFake<ProfileResponse> profileResponseCall = new SuccessCallFake(Response.success(profileResponse));

        when(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall);
        List<String> descriptions = new ArrayList();
        descriptions.add(profileResponse.getOrderHistory().get(0).getDescription());
        when(ordersAdapterFactory.make(
                mainActivity, android.R.layout.simple_list_item_1,
                descriptions)).thenReturn(ordersAdapter);

        subject.onResume(mainActivity);

        verify(foundOrdersBlock).setVisibility(View.VISIBLE);
        verify(noOrdersBlock).setVisibility(View.GONE);

        verify(orders).setAdapter(ordersAdapter);
    }

    private ProfileResponse buildProfileResponseWithoutOrders() {
        ProfileResponse profileResponse = new ProfileResponse(
                "first name",
                "last name",
                "address line 1",
                "address line 2",
                "city",
                "OH",
                "12345",
                new ArrayList<Order>()
        );
        return profileResponse;
    }

    private ProfileResponse buildProfileResponseWithOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order(1, "description"));
        ProfileResponse profileResponse = new ProfileResponse(
                "first name",
                "last name",
                "address line 1",
                "address line 2",
                "city",
                "OH",
                "12345",
                orders
        );
        return profileResponse;
    }
}