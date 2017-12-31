package com.jameskbride.eventbusmvpdemo.main

import android.view.View
import android.widget.*
import com.jameskbride.eventbusmvpdemo.FailureCallFake
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.SuccessCallFake
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.Order
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.jameskbride.eventbusmvpdemo.utils.ToasterWrapper
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.Response
import java.io.IOException

class MainActivityImplTest {

    @Mock private lateinit var mainActivity:MainActivity
    @Mock private lateinit var burritosToGoApi:BurritosToGoApi
    @Mock private lateinit var toasterWrapper:ToasterWrapper
    @Mock private lateinit var customerName:TextView
    @Mock private lateinit var addressLine1:TextView
    @Mock private lateinit var addressLine2:TextView
    @Mock private lateinit var city:TextView
    @Mock private lateinit var state:TextView
    @Mock private lateinit var zipCode:TextView
    @Mock private lateinit var noOrdersBlock:LinearLayout
    @Mock private lateinit var foundOrdersBlock:LinearLayout
    @Mock private lateinit var orders:ListView
    @Mock private lateinit var ordersAdapter:ArrayAdapter<String>
    @Mock private lateinit var ordersAdapterFactory:OrdersAdapterFactory

    private lateinit var subject:MainActivityImpl

    @Before
    fun setUp() {
        initMocks(this)

        subject = MainActivityImpl(burritosToGoApi, toasterWrapper, ordersAdapterFactory)

        whenever(mainActivity.findViewById<TextView>(R.id.customer_name)).thenReturn(customerName)
        whenever(mainActivity.findViewById<TextView>(R.id.address_line_1)).thenReturn(addressLine1)
        whenever(mainActivity.findViewById<TextView>(R.id.address_line_2)).thenReturn(addressLine2)
        whenever(mainActivity.findViewById<TextView>(R.id.city)).thenReturn(city)
        whenever(mainActivity.findViewById<TextView>(R.id.state)).thenReturn(state)
        whenever(mainActivity.findViewById<TextView>(R.id.zipcode)).thenReturn(zipCode)
        whenever(mainActivity.findViewById<LinearLayout>(R.id.no_orders_block)).thenReturn(noOrdersBlock)
        whenever(mainActivity.findViewById<LinearLayout>(R.id.found_orders_block)).thenReturn(foundOrdersBlock)
        whenever(mainActivity.findViewById<ListView>(R.id.order_list)).thenReturn(orders)
    }

    @Test
    fun onCreateSetsTheContentView() {
        subject.onCreate(null, mainActivity)

        verify(mainActivity).setContentView(R.layout.activity_main)
    }

    @Test
    fun onResumeDisplaysAnErrorMessageOnGetProfileFailure() {
        val profileResponseCall = FailureCallFake<ProfileResponse>(IOException("parse exception"))

        whenever(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall)
        whenever(toasterWrapper.makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG)).thenReturn(toasterWrapper)

        subject.onResume(mainActivity)

        verify(toasterWrapper).makeText(mainActivity, R.string.oops, Toast.LENGTH_LONG)
        verify(toasterWrapper).show()
    }

    @Test
    fun onResumeDisplaysTheProfileDetailsOnGetProfileResponse() {
        val profileResponse = buildProfileResponseWithoutOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))

        whenever(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall)

        subject.onResume(mainActivity)

        verify(customerName).setText("${profileResponse.firstName} ${profileResponse.lastName}")
        verify(addressLine1).setText(profileResponse.addressLine1)
        verify(addressLine2).setText(profileResponse.addressLine2)
        verify(city).setText(profileResponse.city)
        verify(state).setText(profileResponse.state)
        verify(zipCode).setText(profileResponse.zipCode)
    }

    @Test
    fun onResumeItDisplaysOrdersWhenTheyAreAvailable() {
        val profileResponse = buildProfileResponseWithOrders()
        val profileResponseCall = SuccessCallFake<ProfileResponse>(Response.success(profileResponse))

        whenever(burritosToGoApi.getProfile(anyString())).thenReturn(profileResponseCall)
        whenever(ordersAdapterFactory.make(
                mainActivity, android.R.layout.simple_list_item_1,
                listOf(profileResponse.orderHistory[0].description))).thenReturn(ordersAdapter)

        subject.onResume(mainActivity)

        verify(foundOrdersBlock).setVisibility(View.VISIBLE)
        verify(noOrdersBlock).setVisibility(View.GONE)

        verify(orders).setAdapter(ordersAdapter)
    }

    private fun buildProfileResponseWithoutOrders(): ProfileResponse {
        val profileResponse = ProfileResponse(
                firstName = "first name",
                lastName = "last name",
                addressLine1 = "address line 1",
                addressLine2 = "address line 2",
                city = "city",
                state = "OH",
                zipCode = "12345"
        )
        return profileResponse
    }

    private fun buildProfileResponseWithOrders(): ProfileResponse {
        val profileResponse = ProfileResponse(
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
        return profileResponse
    }
}