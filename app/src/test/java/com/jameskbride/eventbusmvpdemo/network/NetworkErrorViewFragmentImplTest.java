package com.jameskbride.eventbusmvpdemo.network;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jameskbride.eventbusmvpdemo.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkErrorViewFragmentImplTest {

    @Mock private LayoutInflater layoutInflater;
    @Mock private ViewGroup container;
    @Mock private View view;
    @Mock private NetworkErrorViewFragment networkErrorViewFragment;

    private NetworkErrorViewFragmentImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        subject = new NetworkErrorViewFragmentImpl();
    }

    @Test
    public void onCreateViewInflatesTheView() {
        when(layoutInflater.inflate(R.layout.network_error, container)).thenReturn(view);

        View result = subject.onCreateView(layoutInflater, container, null, networkErrorViewFragment);
        assertSame(view, result);

        verify(layoutInflater).inflate(R.layout.network_error, container);
    }

}