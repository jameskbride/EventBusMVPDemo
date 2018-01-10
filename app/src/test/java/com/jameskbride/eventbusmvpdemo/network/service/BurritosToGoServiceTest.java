package com.jameskbride.eventbusmvpdemo.network.service;

import com.google.gson.Gson;
import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.SecurityErrorEvent;
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.schedulers.TestScheduler;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class BurritosToGoServiceTest {
    
    @Mock private BurritosToGoApi burritosToGoApi;

    private BurritosToGoService subject;

    private EventBus eventBus;
    private TestScheduler testScheduler;

    private boolean getProfileErrorEventFired;
    private GetProfileResponseEvent getProfileResponseEvent;
    private boolean networkErrorEventfired;
    private boolean securityErrorEventFired;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();
        testScheduler = new TestScheduler();

        subject = new BurritosToGoService(eventBus, burritosToGoApi, testScheduler, testScheduler);

        eventBus.register(this);
        subject.open();
    }

    @After
    public void tearDown() {
        eventBus.unregister(this);
        subject.close();
    }

    @Test
    public void itRegistersForGetProfileEvent() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();
        when(burritosToGoApi.getProfile("1")).thenReturn(Observable.just(profileResponse));

        eventBus.post(new GetProfileEvent("1"));
        testScheduler.triggerActions();

        assertNotNull(getProfileResponseEvent);
    }

    @Test
    public void itPostsANetworkErrorEventWhenAnIOExceptionOccurs() {
        IOException throwable = new IOException("parse exception");
        when(burritosToGoApi.getProfile("1")).thenReturn(Observable.<ProfileResponse>error(throwable));

        eventBus.post(new GetProfileEvent("1"));
        testScheduler.triggerActions();

        assertTrue(networkErrorEventfired);
    }

    @Test
    public void itPostsASecurityErrorWhenAnUnauthorizedResponseOccurs() {
        HttpException httpException = new HttpException(Response.error(401, ResponseBody.create(MediaType.parse("application/json"), "")));
        when(burritosToGoApi.getProfile("2")).thenReturn(Observable.<ProfileResponse>error(httpException));

        eventBus.post(new GetProfileEvent("2"));
        testScheduler.triggerActions();

        assertTrue(securityErrorEventFired);
    }

    @Test
    public void onGetProfileEventEmitsGetProfileErrorWhenAFailureOccurs() {
        Gson gson = new Gson();
        gson.toJson(new CustomError());
        Response<Object> throwable = Response.error(400, ResponseBody.create(MediaType.parse("application/json"), gson.toString()));
        HttpException exception = new HttpException(throwable);
        when(burritosToGoApi.getProfile("1")).thenReturn(Observable.<ProfileResponse>error(exception));

        subject.onGetProfileEvent(new GetProfileEvent("1"));
        testScheduler.triggerActions();

        assertTrue(getProfileErrorEventFired);
    }

    @Test
    public void onGetProfileEventEmitsGetProfileResponseEventWhenAResponseIsReceived() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();
        Observable<ProfileResponse> observable = Observable.just(profileResponse);
        when(burritosToGoApi.getProfile("1")).thenReturn(observable);

        subject.onGetProfileEvent(new GetProfileEvent("1"));
        testScheduler.triggerActions();

        assertEquals(profileResponse, getProfileResponseEvent.getProfileResponse());
    }

    @Subscribe
    public void onGetProfileErrorEvent(GetProfileErrorEvent getProfileErrorEvent){
        getProfileErrorEventFired = true;
    }

    @Subscribe
    public void onGetProfileResponseEvent(GetProfileResponseEvent getProfileResponseEvent) {
        this.getProfileResponseEvent = getProfileResponseEvent;
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent networkErrorEvent) {
        networkErrorEventfired = true;
    }

    @Subscribe
    public void onSecurityErrorEvent(SecurityErrorEvent securityErrorEvent) {
        securityErrorEventFired = true;
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

    private static class CustomError {

    }
}