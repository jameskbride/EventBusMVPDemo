package com.jameskbride.eventbusmvpdemo.network.service

import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileEvent
import com.jameskbride.eventbusmvpdemo.bus.GetProfileResponseEvent
import com.jameskbride.eventbusmvpdemo.bus.NetworkErrorEvent
import com.jameskbride.eventbusmvpdemo.network.ProfileApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Observable
import io.reactivex.schedulers.TestScheduler
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.io.IOException
import okhttp3.ResponseBody
import com.google.gson.Gson
import okhttp3.MediaType
import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Response

class BurritosToGoServiceTest {

    @Mock private lateinit var burritosToGoApi:ProfileApi

    private lateinit var subject:ProfileService

    private lateinit var eventBus:EventBus
    private lateinit var testScheduler:TestScheduler

    private var getProfileErrorEventFired: Boolean = false
    private lateinit var getProfileResponseEvent: GetProfileResponseEvent
    private var networkErrorEventfired: Boolean = false

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()
        testScheduler = TestScheduler()

        subject = ProfileService(eventBus, burritosToGoApi, testScheduler, testScheduler)

        eventBus.register(this)
        subject.open()
    }

    @After
    fun tearDown() {
        eventBus.unregister(this)
        subject.close()
    }

    @Test
    fun itRegistersForGetProfileEvent() {
        val profileResponse = buildProfileResponseWithOrders()
        whenever(burritosToGoApi.getProfile("1")).thenReturn(Observable.just(profileResponse))
        eventBus.post(GetProfileEvent("1"))
        testScheduler.triggerActions()

        assertNotNull(getProfileResponseEvent)
    }

    @Test
    fun onGetProfileEventEmitsGetProfileErrorWhenAFailureOccurs() {
        val gson = Gson()
        gson.toJson(CustomError())
        val throwable = Response.error<ProfileResponse>(400, ResponseBody.create(MediaType.parse("application/json"), gson.toString()))
        val exception = HttpException(throwable)
        whenever(burritosToGoApi.getProfile("1")).thenReturn(Observable.error<ProfileResponse>(exception))
        subject.onGetProfileEvent(GetProfileEvent("1"))
        testScheduler.triggerActions()

        assertTrue(getProfileErrorEventFired)
    }

    @Test
    fun onGetProfileEventEmitsGetProfileResponseEventWhenAResponseIsReceived() {
        val profileResponse = buildProfileResponseWithOrders()
        val observable = Observable.just(profileResponse)
        whenever(burritosToGoApi.getProfile("1")).thenReturn(observable)

        subject.onGetProfileEvent(GetProfileEvent("1"))
        testScheduler.triggerActions()

        assertEquals(profileResponse, getProfileResponseEvent.profileResponse)
    }

    @Test
    fun itPostsANetworkErrorEventWhenAnIOExceptionOccurs() {
        val throwable = IOException("parse exception")
        whenever(burritosToGoApi.getProfile("1")).thenReturn(Observable.error<ProfileResponse>(throwable))

        eventBus.post(GetProfileEvent("1"))
        testScheduler.triggerActions()

        assertTrue(networkErrorEventfired)
    }

    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent){
        getProfileErrorEventFired = true
    }

    @Subscribe
    fun onGetProfileResponseEvent(getProfileResponseEvent: GetProfileResponseEvent) {
        this.getProfileResponseEvent = getProfileResponseEvent
    }

    @Subscribe
    fun onNetworkErrorEvent(networkErrorEvent: NetworkErrorEvent) {
        networkErrorEventfired = true
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

class CustomError