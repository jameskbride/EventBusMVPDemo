import android.util.Log

fun waitOn(millis: Long = 10000, action: () -> Unit) {
    var millisLeft = millis

    while(millisLeft >= 0) {
        try {
            action()
            return
        } catch(ex: Exception) {
            val countDownBy = 1000L
            millisLeft -= countDownBy
            Thread.sleep(countDownBy)
            Log.w("EspressoTests", "Action failed")
        }
    }

    action()
}