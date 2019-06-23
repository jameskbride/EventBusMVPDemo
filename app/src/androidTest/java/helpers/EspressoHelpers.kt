import android.util.Log
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*

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