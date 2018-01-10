package com.jameskbride.eventbusmvpdemo.security;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jameskbride.eventbusmvpdemo.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecurityErrorViewFragmentImplTest {

    @Mock private LayoutInflater layoutInflater;
    @Mock private ViewGroup container;
    @Mock private View view;
    @Mock private Button okButton;
    @Mock private SecurityErrorViewPresenter presenter;
    @Mock private SecurityErrorViewFragment securityErrorViewFragment;

    private SecurityErrorViewFragmentImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        subject = new SecurityErrorViewFragmentImpl(presenter);

        when(layoutInflater.inflate(R.layout.security_error, container)).thenReturn(view);
        when(view.findViewById(R.id.ok_button)).thenReturn(okButton);

        subject.onCreateView(layoutInflater, container, null, securityErrorViewFragment);
    }

    @Test
    public void onCreateViewInflatesTheView() {
        reset(layoutInflater);
        when(layoutInflater.inflate(R.layout.security_error, container)).thenReturn(view);
        View result = subject.onCreateView(layoutInflater, container, null, securityErrorViewFragment);
        assertSame(view, result);

        verify(layoutInflater).inflate(R.layout.security_error, container);
    }

    @Test
    public void onCreateViewConfiguresTheOkButton() {
        ArgumentCaptor<View.OnClickListener> retryButtonCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);

        verify(okButton).setOnClickListener(retryButtonCaptor.capture());

        retryButtonCaptor.getValue().onClick(null);

        verify(presenter).dismiss();
    }
}