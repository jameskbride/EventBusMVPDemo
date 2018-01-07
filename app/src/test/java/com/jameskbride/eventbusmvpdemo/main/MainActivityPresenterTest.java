package com.jameskbride.eventbusmvpdemo.main;

import com.jameskbride.eventbusmvpdemo.FailureCallFake;
import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.SuccessCallFake;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class MainActivityPresenterTest {

    @Mock BurritosToGoApi burritosToGoApi;
    @Mock private MainActivityView view;

    MainActivityPresenter subject;

    @Before
    public void setUp() {
        initMocks(this);
        subject = new MainActivityPresenter(burritosToGoApi);
        subject.view = view;
    }

    @Test
    public void itDisplaysAnErrorViewOnFailureOfGetProfile() {
        Call<ProfileResponse> profileResponseCall = new FailureCallFake(new IOException("parse exception"));
        when(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall);

        subject.getProfile("1");

        verify(view).displayError(R.string.oops);
    }

    @Test
    public void itDisplaysProfileDetailsOnResponseOfGetProfile() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();
        Call<ProfileResponse> profileResponseCall = new SuccessCallFake(Response.success(profileResponse));
        when(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall);

        subject.getProfile("1");

        verify(view).displayProfileDetails(profileResponse);
    }

    @Test
    public void itDisplaysTheOrdersViewOnResponseOfGetProfileWithOrders() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();
        Call<ProfileResponse> profileResponseCall = new SuccessCallFake(Response.success(profileResponse));
        when(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall);

        subject.getProfile("1");

        verify(view).displayOrders(profileResponse.getOrderHistory());
    }

    @Test
    public void itDisplaysTheNoOrdersFoundViewOnResponseOfGetProfileWithoutOrders() {
        ProfileResponse profileResponse = buildProfileResponseWithoutOrders();
        Call<ProfileResponse> profileResponseCall = new SuccessCallFake(Response.success(profileResponse));
        when(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall);

        subject.getProfile("1");

        verify(view).displayNoOrders();
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