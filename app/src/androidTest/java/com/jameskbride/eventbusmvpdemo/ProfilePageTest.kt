package com.jameskbride.eventbusmvpdemo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.jameskbride.eventbusmvpdemo.main.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
}
