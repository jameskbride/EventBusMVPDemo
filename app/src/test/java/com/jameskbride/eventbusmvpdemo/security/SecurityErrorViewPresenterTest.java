package com.jameskbride.eventbusmvpdemo.security;

import org.greenrobot.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecurityErrorViewPresenterTest {

    @Mock private SecurityErrorViewPresenter.SecurityErrorView view;

    private EventBus eventBus;

    private SecurityErrorViewPresenter subject;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();

        subject = new SecurityErrorViewPresenter(eventBus);
        subject.view = view;
    }

    @Test
    public void onDismissItDismissesTheDialog() {
        subject.dismiss();

        verify(view).dismiss();
    }
}