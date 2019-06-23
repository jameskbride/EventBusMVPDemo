package com.jameskbride.eventbusmvpdemo.pages

import com.jameskbride.eventbusmvpdemo.R
import shouldDisplayText

class NetworkErrorPage {
    fun displaysErrorMessage(errorMessage: String) {
        shouldDisplayText(errorMessage)
    }

    fun retryButton() = R.id.retry_button
}