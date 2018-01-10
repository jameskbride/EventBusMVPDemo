package com.jameskbride.eventbusmvpdemo.network;

import com.jameskbride.eventbusmvpdemo.bus.SecurityErrorEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

public class SecurityApiWrapperTest {

    @Mock private TameConsumer tameConsumer;

    private SecurityApiWrapper subject;

    private EventBus eventBus;
    private SecurityErrorEvent securityErrorEvent;

    @Before
    public void setUp() {
        initMocks(this);
        eventBus = EventBus.getDefault();

        subject = new SecurityApiWrapper(eventBus, tameConsumer);

        eventBus.register(this);
    }

    @After
    public void tearDown() {
        eventBus.unregister(this);
    }

    @Test
    public void givenAnUnauthorizedResponseThenItPostsASecurityErrorEvent() throws Exception {
        HttpException httpException = new HttpException(Response.error(401, ResponseBody.create(MediaType.parse("application/json"), "")));

        subject.accept(httpException);

        assertNotNull(securityErrorEvent);
    }

    @Test
    public void givenANonSecurityErrorOccurredThenItDelegatesToTheNextConsumer() throws Exception {
        HttpException httpException = new HttpException(Response.error(500, ResponseBody.create(MediaType.parse("application/json"), "")));
        subject.accept(httpException);

        verify(tameConsumer).accept(httpException);
    }

    @Test
    public void givenANonHttpExceptionOccurredThenItDelegatesToTheNextConsumer() throws Exception {
        IOException exception = new IOException("broken pipe");
        subject.accept(exception);

        verify(tameConsumer).accept(exception);
    }

    @Subscribe
    public void onSecurityErrorEvent(SecurityErrorEvent securityErrorEvent) {
        this.securityErrorEvent = securityErrorEvent;
    }

    private static class TameConsumer implements Consumer<Throwable> {

        @Override
        public void accept(Throwable throwable) throws Exception {

        }
    }
}