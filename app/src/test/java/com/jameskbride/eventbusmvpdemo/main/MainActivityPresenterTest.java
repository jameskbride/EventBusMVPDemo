package com.jameskbride.eventbusmvpdemo.main;

import com.jameskbride.eventbusmvpdemo.GetProfileResponseEvent;
import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent;
import com.jameskbride.eventbusmvpdemo.network.Order;
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class MainActivityPresenterTest {

    @Mock private MainActivityView view;

    MainActivityPresenter subject;

    private EventBus eventBus;

    private GetProfileEvent getProfileEvent;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();
        subject = new MainActivityPresenter(eventBus);
        subject.view = view;

        eventBus.register(this);
    }

    @After
    public void tearDown() {
        eventBus.unregister(this);
    }

    @Test
    public void openRegistersWithTheBus() {
        eventBus.unregister(subject);

        subject.open();

        assertTrue(eventBus.isRegistered(subject));
    }

    @Test
    public void closeUnregistersWithTheBus() {
        eventBus.register(subject);

        subject.close();

        assertFalse(eventBus.isRegistered(subject));
    }

    @Test
    public void itDisplaysAnErrorViewOnFailureOfGetProfile() {
        subject.onGetProfileErrorEvent(new GetProfileErrorEvent());

        verify(view).displayError(R.string.oops);
    }

    @Test
    public void itDisplaysProfileDetailsOnResponseOfGetProfile() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();

        subject.onGetProfileResponseEvent(new GetProfileResponseEvent(profileResponse));

        verify(view).displayProfileDetails(profileResponse);
    }

    @Test
    public void itDisplaysTheOrdersViewOnResponseOfGetProfileWithOrders() {
        ProfileResponse profileResponse = buildProfileResponseWithOrders();

        subject.onGetProfileResponseEvent(new GetProfileResponseEvent(profileResponse));

        verify(view).displayOrders(profileResponse.getOrderHistory());
    }

    @Test
    public void itDisplaysTheNoOrdersFoundViewOnResponseOfGetProfileWithoutOrders() {
        ProfileResponse profileResponse = buildProfileResponseWithoutOrders();

        subject.onGetProfileResponseEvent(new GetProfileResponseEvent(profileResponse));

        verify(view).displayNoOrders();
    }

    @Test
    public void itCanGetTheProfile() {
        subject.getProfile("1");

        assertEquals("1", getProfileEvent.getId());
    }

    @Subscribe
    public void onGetProfileEvent(GetProfileEvent getProfileEvent) {
        this.getProfileEvent = getProfileEvent;
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