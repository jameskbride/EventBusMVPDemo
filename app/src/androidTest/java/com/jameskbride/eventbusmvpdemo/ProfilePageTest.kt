package com.jameskbride.eventbusmvpdemo

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jameskbride.eventbusmvpdemo.main.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import waitOn

@RunWith(AndroidJUnit4::class)
class ProfilePageTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java,true, true)

    @Test
    fun whenTheViewLoadsItDisplaysTheSearchView() {
        onView(withId(R.id.profile_id_edit)).check(matches(isDisplayed()))
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheProfile() {
        onView(withId(R.id.profile_id_edit)).perform(typeText("1"))

        onView(withId(R.id.submit)).perform(click())

        onView(withText("Walter White")).check(matches(isDisplayed()))
        onView(withText("123 Street Ln")).check(matches(isDisplayed()))
        onView(withText("Albuquerque")).check(matches(isDisplayed()))
        onView(withText("NM")).check(matches(isDisplayed()))
        onView(withText("87101")).check(matches(isDisplayed()))
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheOrders() {
        onView(withId(R.id.profile_id_edit)).perform(typeText("1"))

        onView(withId(R.id.submit)).perform(click())
        Espresso.closeSoftKeyboard()

        onView(withText("Large Pizza")).perform(ViewActions.scrollTo()).check(matches(isDisplayed()))
        onView(withText("Industrial size beaker")).perform(ViewActions.scrollTo()).check(matches(isDisplayed()))
        onView(withText("Barrel of Methylamine")).perform(ViewActions.scrollTo()).check(matches(isDisplayed()))
    }

    @Test
    fun givenAnInvalidProfileIdItDisplaysTheNetworkErrorView() {
        onView(withId(R.id.profile_id_edit)).perform(typeText("-1"))

        onView(withId(R.id.submit)).perform(click())
        Espresso.closeSoftKeyboard()

        waitOn {
            onView(withText("Oops, something went wrong!")).check(matches(isDisplayed()))
            onView(withId(R.id.retry_button)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun givenTheSecurityCheckFailedItDisplaysTheSecurityErrorView() {
        onView(withId(R.id.profile_id_edit)).perform(typeText("2"))

        onView(withId(R.id.submit)).perform(click())
        Espresso.closeSoftKeyboard()

        waitOn {
            onView(allOf(withId(R.id.security_error_message), withText("Please log in"))).check(matches(isDisplayed()))
            onView(withId(R.id.ok_button)).check(matches(isDisplayed()))
        }
    }
}
