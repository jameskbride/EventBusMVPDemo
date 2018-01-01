package com.jameskbride.eventbusmvpdemo.main

import com.jameskbride.eventbusmvpdemo.FailureCallFake
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.network.BurritosToGoApi
import com.jameskbride.eventbusmvpdemo.network.ProfileResponse
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks
import java.io.IOException

class MainActivityPresenterTest {

    @Mock private lateinit var burritosToGoApi:BurritosToGoApi
    @Mock private lateinit var view:MainActivityView

    private lateinit var subject:MainActivityPresenter

    @Before
    fun setUp() {
        initMocks(this)
        subject = MainActivityPresenter(burritosToGoApi)
        subject.view = view
    }

    @Test
    fun itDisplaysAnErrorViewOnFailureOfGetProfile() {
        val profileResponseCall = FailureCallFake<ProfileResponse>(IOException("parse exception"))

        whenever(burritosToGoApi.getProfile("1")).thenReturn(profileResponseCall)

        subject.getProfile("1")

        verify(view).displayError(R.string.oops)
    }
}