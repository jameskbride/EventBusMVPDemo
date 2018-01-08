package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkErrorViewPresenterTest {

    @Mock private NetworkErrorViewPresenter.NetworkErrorView view;

    private EventBus eventBus;

    private NetworkErrorViewPresenter subject;
    private NetworkRequestEvent retryRequest;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();

        subject = new NetworkErrorViewPresenter(eventBus);
        subject.view = view;

        eventBus.register(this);
    }

    @After
    public void tearDown() {
        eventBus.unregister(this);
    }

    @Test
    public void onRetryItPostsTheNetworkRequestEvent() {
        NetworkRequestEvent networkRequestEvent = new NetworkRequestEvent();

        subject.retry(networkRequestEvent);

        assertSame(networkRequestEvent, this.retryRequest);
    }

    @Test
    public void onRetryItDismissesTheDialog() {
        subject.retry(new NetworkRequestEvent());

        verify(view).dismiss();
    }

    @Subscribe
    public void onNetworkRequestEvent(NetworkRequestEvent networkRequestEvent) {
        this.retryRequest = networkRequestEvent;
    }
}