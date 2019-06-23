import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.util.Log

fun waitOn(millis: Long = 10000, action: () -> Unit) {
    var millisLeft = millis

    while (millisLeft >= 0) {
        try {
            action()
            return
        } catch (ex: Exception) {
            val countDownBy = 1000L
            millisLeft -= countDownBy
            Thread.sleep(countDownBy)
            Log.w("EspressoTests", "Action failed")
        }
    }

    action()
}

fun shouldScrollToAndDisplay(text: String) {
    onView(withText(text)).perform(scrollTo()).check(ViewAssertions.matches(isDisplayed()))
}

fun shouldDisplayText(text: String) {
    onView(withText(text)).check(ViewAssertions.matches(isDisplayed()))
}

fun shouldDisplay(retryButton: Int) {
    onView(withId(retryButton)).check(ViewAssertions.matches(isDisplayed()))
}

fun clickOn(submit: Int) {
    onView(withId(submit)).perform(click())
    Espresso.closeSoftKeyboard()
}

fun typeInto(profileIdEdit: Int, profileId: String) {
    onView(withId(profileIdEdit)).perform(typeText(profileId))
    Espresso.closeSoftKeyboard()
}