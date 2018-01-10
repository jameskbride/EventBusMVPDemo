package com.jameskbride.eventbusmvpdemo.main

import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.bus.*
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.verify
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks


class MainActivityPresenterTest {

    @Mock private lateinit var view:MainActivityView

    private lateinit var eventBus:EventBus

    private lateinit var subject:MainActivityPresenter

    private lateinit var getProfileEvent: GetProfileEvent

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        subject = MainActivityPresenter(eventBus)
        subject.view = view

        eventBus.register(this)
    }

    @After
    fun tearDown() {
        eventBus.unregister(this)
    }

    @Test
    fun openRegistersWithTheBus() {
        eventBus.unregister(subject)

        subject.open()

        assertTrue(eventBus.isRegistered(subject))
    }

    @Test
    fun closeUnregistersWithTheBus() {
        eventBus.register(subject)

        subject.close()

        assertFalse(eventBus.isRegistered(subject))
    }

    @Test
    fun itDisplaysAnErrorViewOnFailureOfGetProfile() {
        subject.onGetProfileErrorEvent(GetProfileErrorEvent())

        verify(view).displayError(R.string.oops)
    }

    @Test
    fun itDisplaysProfileDetailsOnResponseOfGetProfile() {
        val profileResponse = buildProfileResponseWithOrders()

        subject.onGetProfileResponseEvent(GetProfileResponseEvent(profileResponse))

        verify(view).displayProfileDetails(profileResponse)
    }

    @Test
    fun itDisplaysTheOrdersViewOnResponseOfGetProfileWithOrders() {
        val profileResponse = buildProfileResponseWithOrders()

        subject.onGetProfileResponseEvent(GetProfileResponseEvent(profileResponse))

        verify(view).displayOrders(profileResponse.orderHistory)
    }

    @Test
    fun itDisplaysTheNoOrdersFoundViewOnResponseOfGetProfileWithoutOrders() {
        val profileResponse = buildProfileResponseWithoutOrders()

        subject.onGetProfileResponseEvent(GetProfileResponseEvent(profileResponse))

        verify(view).displayNoOrders()
    }

    @Test
    fun itCanGetTheProfile() {
        subject.getProfile("1")

        assertEquals("1", getProfileEvent.id)
    }

    @Test
    fun itDisplaysTheNetworkErrorViewWhenANetworkErrorEventIsReceived() {
        val networkErrorEvent = NetworkErrorEvent(GetProfileEvent("1"))
        subject.onNetworkErrorEvent(networkErrorEvent)

        verify(view).displayNetworkError(networkErrorEvent)
    }

    @Test
    fun itDisplaysTheSecurityErrorViewWhenASecurityErrorEventIsReceived() {
        val securityErrorEvent = SecurityErrorEvent()

        subject.onSecurityErrorEvent(securityErrorEvent)

        verify(view).displaySecurityError()
    }

    @Subscribe
    fun onGetProfileEvent(getProfileEvent:GetProfileEvent) {
        this.getProfileEvent = getProfileEvent
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