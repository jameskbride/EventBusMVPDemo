package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.FailureCallFake
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.whenever
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.io.IOException

class BurritosToGoServiceTest {

    @Mock private lateinit var burritosToGoApi:BurritosToGoApi

    private lateinit var subject:BurritosToGoService

    private lateinit var eventBus:EventBus

    private var getProfileErrorEventFired: Boolean = false

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        subject = BurritosToGoService(eventBus, burritosToGoApi)

        eventBus.register(this)
    }

    @After
    fun tearDown() {
        eventBus.unregister(this)
    }

    @Test
    fun onGetProfileEventEmitsGetProfileErrorWhenAFailureOccurs() {
        val profileResponseCall = FailureCallFake<ProfileResponse>(IOException("parse exception"))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.onGetProfileEvent(GetProfileEvent("1"))

        assertTrue(getProfileErrorEventFired)
    }

    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent){
        getProfileErrorEventFired = true
    }
}