package com.jameskbride.eventbusmvpdemo.network.service;

import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
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

import static org.junit.Assert.assertEquals;
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
        IOException throwable = new IOException("parse exception");
        when(burritosToGoApi.getProfile("1")).thenReturn(Observable.<ProfileResponse>error(throwable));

        eventBus.post(new GetProfileEvent("1"));
        testScheduler.triggerActions();

        assertTrue(getProfileErrorEventFired);
    }

    @Test
    public void onGetProfileEventEmitsGetProfileErrorWhenAFailureOccurs() {
        IOException throwable = new IOException("parse exception");
        when(burritosToGoApi.getProfile("1")).thenReturn(Observable.<ProfileResponse>error(throwable));

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