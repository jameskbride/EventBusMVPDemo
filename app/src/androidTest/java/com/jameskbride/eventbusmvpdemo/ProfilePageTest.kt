package com.jameskbride.eventbusmvpdemo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import clickOn
import com.jameskbride.eventbusmvpdemo.main.MainActivity
import org.hamcrest.core.AllOf.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import shouldDisplay
import shouldDisplayText
import shouldScrollToAndDisplay
import typeInto
import waitOn

@RunWith(AndroidJUnit4::class)
class ProfilePageTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java,true, true)

    @Test
    fun whenTheViewLoadsItDisplaysTheSearchView() {
        shouldDisplay(R.id.profile_id_edit)
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheProfile() {
        typeInto(R.id.profile_id_edit, "1")

        clickOn(R.id.submit)

        shouldDisplayText("Walter White")
        shouldDisplayText("123 Street Ln")
        shouldDisplayText("Albuquerque")
        shouldDisplayText("NM")
        shouldDisplayText("87101")
    }

    @Test
    fun givenAGoodProfileIdItDisplaysTheOrders() {
        typeInto(R.id.profile_id_edit, "1")

        clickOn(R.id.submit)

        shouldScrollToAndDisplay("Large Pizza")
        shouldScrollToAndDisplay("Industrial size beaker")
        shouldScrollToAndDisplay("Barrel of Methylamine")
    }

    @Test
    fun givenAnInvalidProfileIdItDisplaysTheNetworkErrorView() {
        typeInto(R.id.profile_id_edit, "-1")

        clickOn(R.id.submit)

        waitOn {
            shouldDisplayText("Oops, something went wrong!")
            shouldDisplay(R.id.retry_button)
        }
    }

    @Test
    fun givenTheSecurityCheckFailedItDisplaysTheSecurityErrorView() {
        typeInto(R.id.profile_id_edit, "2")

        clickOn(R.id.submit)

        waitOn {
            onView(allOf(withId(R.id.security_error_message), withText("Please log in"))).check(matches(isDisplayed()))
            shouldDisplay(R.id.ok_button)
        }
    }
}
