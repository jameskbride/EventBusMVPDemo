package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations.initMocks
import java.io.IOException


class NetworkApiWrapperTest {

    private lateinit var subject: NetworkApiWrapper<Throwable>

    private lateinit var eventBus: EventBus
    private lateinit var networkRequestEvent: NetworkRequestEvent
    private lateinit var networkErrorEvent: NetworkErrorEvent

    private var getProfileErrorEventFired: Boolean = false

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()
        networkRequestEvent = NetworkRequestEvent()

        subject = NetworkApiWrapper(eventBus,
                { error -> eventBus.post(GetProfileErrorEvent()) },
                networkRequestEvent)

        eventBus!!.register(this)
    }

    @After
    fun tearDown() {
        eventBus!!.unregister(this)
    }

    @Test
    fun givenAnIOExceptionOccurredThenANetworkErrorEventIsPosted() {
        subject.accept(IOException("Broken pipe"))

        assertEquals(networkRequestEvent, networkErrorEvent!!.networkRequestEvent)
    }

    @Test
    fun givenANonIOExceptionOccurredThenItDelegatesToTheNextConsumer() {
        val exception = IllegalAccessException("Some other exception")
        subject.accept(exception)

        assertTrue(getProfileErrorEventFired)
    }

    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent) {
        this.getProfileErrorEventFired = true
    }

    @Subscribe
    fun onNetworkErrorEvent(networkErrorEvent: NetworkErrorEvent) {
        this.networkErrorEvent = networkErrorEvent
    }
}