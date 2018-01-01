package com.jameskbride.eventbusmvpdemo.main

import com.jameskbride.eventbusmvpdemo.FailureCallFake
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.SuccessCallFake
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.greenrobot.eventbus.EventBus
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Response
import java.io.IOException

class MainActivityPresenterTest {

    @Mock private lateinit var burritosToGoApi:BurritosToGoApi
    @Mock private lateinit var view:MainActivityView

    private lateinit var eventBus:EventBus

    private lateinit var subject:MainActivityPresenter

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        subject = MainActivityPresenter(burritosToGoApi, eventBus)
        subject.view = view
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
        val profileResponseCall = FailureCallFake<ProfileResponse>(IOException("parse exception"))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.getProfile("1")

        verify(view).displayError(R.string.oops)
    }

    @Test
    fun itDisplaysProfileDetailsOnResponseOfGetProfile() {
        val profileResponse = buildProfileResponseWithOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.getProfile("1")

        verify(view).displayProfileDetails(profileResponse)
    }

    @Test
    fun itDisplaysTheOrdersViewOnResponseOfGetProfileWithOrders() {
        val profileResponse = buildProfileResponseWithOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.getProfile("1")

        verify(view).displayOrders(profileResponse.orderHistory)
    }

    @Test
    fun itDisplaysTheNoOrdersFoundViewOnResponseOfGetProfileWithoutOrders() {
        val profileResponse = buildProfileResponseWithoutOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))
        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.getProfile("1")

        verify(view).displayNoOrders()
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