package com.jameskbride.eventbusmvpdemo.security

import com.nhaarman.mockito_kotlin.verify
import org.greenrobot.eventbus.EventBus
import org.mockito.MockitoAnnotations.initMocks
import org.junit.Before
import org.junit.Test
import org.mockito.Mock

class SecurityErrorViewPresenterTest {

    @Mock private lateinit var view: SecurityErrorViewPresenter.SecurityErrorView

    private lateinit var eventBus: EventBus

    private lateinit var subject: SecurityErrorViewPresenter

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        subject = SecurityErrorViewPresenter(eventBus)
        subject.view = view
    }

    @Test
    fun onDismissItDismissesTheDialog() {
        subject.dismiss()

        verify(view).dismiss()
    }
}