package com.jameskbride.eventbusmvpdemo.network

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jameskbride.eventbusmvpdemo.R
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent
import com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.Companion.NETWORK_REQUEST
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.reset
import org.mockito.MockitoAnnotations.initMocks


class NetworkErrorViewFragmentImplTest {

    @Mock private lateinit var layoutInflater: LayoutInflater
    @Mock private lateinit var container: ViewGroup
    @Mock private lateinit var view: View
    @Mock private lateinit var retryButton: Button
    @Mock private lateinit var bundle: Bundle
    @Mock private lateinit var networkErrorViewFragment: NetworkErrorViewFragment
    @Mock private lateinit var presenter: NetworkErrorViewPresenter

    private lateinit var networkRequestEvent: NetworkRequestEvent

    private lateinit var subject: NetworkErrorViewFragmentImpl

    @Before
    fun setUp() {
        initMocks(this)

        networkRequestEvent = NetworkRequestEvent()

        subject = NetworkErrorViewFragmentImpl(presenter)

        whenever(bundle.getSerializable(NETWORK_REQUEST)).thenReturn(networkRequestEvent)
        whenever(layoutInflater.inflate(R.layout.network_error, container)).thenReturn(view)
        whenever(view.findViewById<Button>(R.id.retry_button)).thenReturn(retryButton)
        whenever(networkErrorViewFragment.arguments).thenReturn(bundle)

        subject!!.onCreateView(layoutInflater, container, bundle, networkErrorViewFragment!!)
    }

    @Test
    fun onCreateViewInflatesTheView() {
        reset(layoutInflater)
        whenever(layoutInflater.inflate(R.layout.network_error, container)).thenReturn(view)
        val result = subject.onCreateView(layoutInflater, container, bundle, networkErrorViewFragment!!)
        assertSame(view, result)

        verify(layoutInflater).inflate(R.layout.network_error, container)
    }

    @Test
    fun onCreateViewConfiguresTheRetryButton() {
        val retryButtonCaptor = argumentCaptor<View.OnClickListener>()

        verify(retryButton).setOnClickListener(retryButtonCaptor.capture())

        retryButtonCaptor.firstValue.onClick(null)

        verify(presenter).retry(networkRequestEvent)
    }

    @Test
    fun itCanDismissTheDialog() {
        subject.dismiss()

        verify(networkErrorViewFragment).dismiss()
    }
}