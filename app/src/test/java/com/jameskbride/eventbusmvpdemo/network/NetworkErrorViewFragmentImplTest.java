package com.jameskbride.eventbusmvpdemo.network;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jameskbride.eventbusmvpdemo.R;
import com.jameskbride.eventbusmvpdemo.bus.NetworkRequestEvent;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static com.jameskbride.eventbusmvpdemo.network.NetworkErrorViewFragment.NETWORK_REQUEST;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class NetworkErrorViewFragmentImplTest {

    @Mock private LayoutInflater layoutInflater;
    @Mock private ViewGroup container;
    @Mock private View view;
    @Mock private Button retryButton;
    @Mock private Bundle bundle;
    @Mock private NetworkErrorViewFragment networkErrorViewFragment;
    @Mock private NetworkErrorViewPresenter presenter;

    private NetworkRequestEvent networkRequestEvent;

    private NetworkErrorViewFragmentImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        networkRequestEvent = new NetworkRequestEvent();

        subject = new NetworkErrorViewFragmentImpl(presenter);

        when(bundle.getSerializable(NETWORK_REQUEST)).thenReturn(networkRequestEvent);
        when(layoutInflater.inflate(R.layout.network_error, container)).thenReturn(view);
        when(view.findViewById(R.id.retry_button)).thenReturn(retryButton);
        when(networkErrorViewFragment.getArguments()).thenReturn(bundle);
    }

    @Test
    public void onCreateViewInflatesTheView() {
        View result = subject.onCreateView(layoutInflater, container, null, networkErrorViewFragment);
        assertSame(view, result);

        verify(layoutInflater).inflate(R.layout.network_error, container);
    }

    @Test
    public void onCreateViewConfiguresTheRetryButton() {
        subject.onCreateView(layoutInflater, container, null, networkErrorViewFragment);

        ArgumentCaptor<View.OnClickListener> retryButtonCaptor = ArgumentCaptor.forClass(View.OnClickListener.class);

        verify(retryButton).setOnClickListener(retryButtonCaptor.capture());

        retryButtonCaptor.getValue().onClick(null);

        verify(presenter).retry(networkRequestEvent);
    }

}