package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.FailureCallFake
import com.jameskbride.eventbusmvpdemo.SuccessCallFake
import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.whenever
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Response
import java.io.IOException

class BurritosToGoServiceTest {

    @Mock private lateinit var burritosToGoApi:BurritosToGoApi

    private lateinit var subject:BurritosToGoService

    private lateinit var eventBus:EventBus

    private var getProfileErrorEventFired: Boolean = false
    private lateinit var getProfileResponseEvent: GetProfileResponseEvent

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

    @Test
    fun onGetProfileEventEmitsGetProfileResponseEventWhenAResponseIsReceived() {
        val profileResponse = buildProfileResponseWithOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.onGetProfileEvent(GetProfileEvent("1"))

        assertEquals(profileResponse, getProfileResponseEvent.profileResponse)
    }

    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent){
        getProfileErrorEventFired = true
    }

    @Subscribe
    fun onGetProfileResponseEvent(getProfileResponseEvent: GetProfileResponseEvent) {
        this.getProfileResponseEvent = getProfileResponseEvent
    }

    private fun buildProfileResponseWithoutOrders(): ProfileResponse {
        return ProfileResponse(
                firstName = "first name",
                lastName = "last name",
                addressLine1 = "address line 1",
                addressLine2 = "address line 2",
                city = "city",
                state = "OH",
                zipCode = "12345"
        )
    }

    private fun buildProfileResponseWithOrders(): ProfileResponse {
        return ProfileResponse(
                firstName = "first name",
                lastName = "last name",
                addressLine1 = "address line 1",
                addressLine2 = "address line 2",
                city = "city",
                state = "OH",
                zipCode = "12345",
                orderHistory = listOf(
                        Order(1, description = "description")
                )
        )
    }
}