package com.jameskbride.eventbusmvpdemo.network

import com.jameskbride.eventbusmvpdemo.bus.GetProfileErrorEvent
import com.jameskbride.eventbusmvpdemo.bus.SecurityErrorEvent
import io.reactivex.functions.Consumer
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations.initMocks
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException


class SecurityApiWrapperTest {

    private lateinit var subject: SecurityApiWrapper<Throwable>

    private lateinit var eventBus: EventBus
    private lateinit var securityErrorEvent: SecurityErrorEvent

    private var getProfileErrorEventFired: Boolean = false

    @Before
    fun setUp() {
        initMocks(this)
        eventBus = EventBus.getDefault()

        val errorConsumer = Consumer<Throwable> { error -> eventBus.post(GetProfileErrorEvent()) }

        subject = SecurityApiWrapper(eventBus, errorConsumer)

        eventBus.register(this)
    }

    @After
    fun tearDown() {
        eventBus.unregister(this)
    }

    @Test
    fun givenAnUnauthorizedResponseThenItPostsASecurityErrorEvent() {
        val httpException = HttpException(Response.error<ProfileResponse>(401, ResponseBody.create(MediaType.parse("application/json"), "")))

        subject.accept(httpException)

        assertNotNull(securityErrorEvent)
    }

    @Test
    fun givenANonSecurityErrorOccurredThenItDelegatesToTheNextConsumer() {
        val httpException = HttpException(Response.error<ProfileResponse>(500, ResponseBody.create(MediaType.parse("application/json"), "")))
        subject.accept(httpException)

        assertTrue(getProfileErrorEventFired)
    }

    @Test
    fun givenANonHttpExceptionOccurredThenItDelegatesToTheNextConsumer() {
        val exception = IOException("broken pipe")
        subject.accept(exception)

        assertTrue(getProfileErrorEventFired)
    }

    @Subscribe
    fun onSecurityErrorEvent(securityErrorEvent: SecurityErrorEvent) {
        this.securityErrorEvent = securityErrorEvent
    }


    @Subscribe
    fun onGetProfileErrorEvent(getProfileErrorEvent: GetProfileErrorEvent) {
        this.getProfileErrorEventFired = true
    }
}