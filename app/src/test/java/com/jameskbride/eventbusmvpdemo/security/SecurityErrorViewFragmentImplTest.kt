package com.jameskbride.eventbusmvpdemo.security

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jameskbride.eventbusmvpdemo.R
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.reset
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import org.junit.Assert.assertSame
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations.initMocks

class SecurityErrorViewFragmentImplTest {

    @Mock private lateinit var layoutInflater: LayoutInflater
    @Mock private lateinit var container: ViewGroup
    @Mock private lateinit var view: View
    @Mock private lateinit var okButton: Button
    @Mock private lateinit var bundle:Bundle
    @Mock private lateinit var presenter: SecurityErrorViewPresenter
    @Mock private lateinit var securityErrorViewFragment: SecurityErrorViewFragment

    private lateinit var subject: SecurityErrorViewFragmentImpl

    @Before
    fun setUp() {
        initMocks(this)

        subject = SecurityErrorViewFragmentImpl(presenter)

        whenever(layoutInflater.inflate(R.layout.security_error, container)).thenReturn(view)
        whenever(view.findViewById<Button>(R.id.ok_button)).thenReturn(okButton)

        subject.onCreateView(layoutInflater, container, bundle, securityErrorViewFragment)
    }

    @Test
    fun onCreateViewInflatesTheView() {
        reset(layoutInflater)
        whenever(layoutInflater.inflate(R.layout.security_error, container)).thenReturn(view)
        val result = subject.onCreateView(layoutInflater, container, bundle, securityErrorViewFragment)
        assertSame(view, result)

        verify(layoutInflater).inflate(R.layout.security_error, container)
    }

    @Test
    fun onCreateViewConfiguresTheOkButton() {
        val retryButtonCaptor = argumentCaptor<View.OnClickListener>()

        verify(okButton).setOnClickListener(retryButtonCaptor.capture())

        retryButtonCaptor.firstValue.onClick(null)

        verify(presenter).dismiss()
    }
}