package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import com.nhaarman.mockito_kotlin.verify
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Assert.assertSame
import org.mockito.MockitoAnnotations.initMocks
import org.junit.Before
import org.junit.Test
import org.mockito.Mock


class NetworkErrorViewPresenterTest {

    @Mock private lateinit var view: NetworkErrorView

    private lateinit var eventBus: EventBus

    private lateinit var subject: NetworkErrorViewPresenter
    private lateinit var retryRequest: NetworkRequestEvent

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        subject = NetworkErrorViewPresenter(eventBus)
        subject.view = view

        eventBus.register(this)
    }

    @After
    fun tearDown() {
        eventBus.unregister(this)
    }

    @Test
    fun onRetryItPostsTheNetworkRequestEvent() {
        val networkRequestEvent = NetworkRequestEvent()

        subject.retry(networkRequestEvent)

        assertSame(networkRequestEvent, this.retryRequest)
    }

    @Test
    fun onRetryItDismissesTheDialog() {
        subject.retry(NetworkRequestEvent())

        verify(view).dismiss()
    }

    @Subscribe
    fun onNetworkRequestEvent(networkRequestEvent: NetworkRequestEvent) {
        this.retryRequest = networkRequestEvent
    }
}