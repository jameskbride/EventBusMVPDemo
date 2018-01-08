package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import io.reactivex.functions.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkApiWrapperTest {

    @Mock private TameConsumer tameConsumer;

    private NetworkApiWrapper subject;

    private EventBus eventBus;
    private NetworkRequestEvent networkRequestEvent;
    private NetworkErrorEvent networkErrorEvent;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();
        networkRequestEvent = new NetworkRequestEvent();

        subject = new NetworkApiWrapper(eventBus, tameConsumer, networkRequestEvent);

        eventBus.register(this);
    }

    @After
    public void tearDown() {
        eventBus.unregister(this);
    }

    @Test
    public void givenAnIOExceptionOccurredThenANetworkErrorEventIsPosted() throws Exception {
        subject.accept(new IOException("Broken pipe"));

        assertEquals(networkRequestEvent, networkErrorEvent.getNetworkRequestEvent());
    }

    @Test
    public void givenANonIOExceptionOccurredThenItDelegatesToTheNextConsumer() throws Exception {
        IllegalAccessException exception = new IllegalAccessException("Some other exception");
        subject.accept(exception);

        verify(tameConsumer).accept(exception);
    }

    @Subscribe
    public void onNetworkErrorEvent(NetworkErrorEvent networkErrorEvent) {
        this.networkErrorEvent = networkErrorEvent;
    }

    private static class TameConsumer implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) throws Exception {

        }
    }
}