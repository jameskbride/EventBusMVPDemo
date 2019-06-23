package com.jameskbride.eventbusmvpdemo.pages

import com.jameskbride.eventbusmvpdemo.R
import shouldDisplayTextFor

class SecurityErrorPage {

    fun displaysSecurityMessage(securityErrorMessage: String) {
        val securityErrorId = R.id.security_error_message
        shouldDisplayTextFor(securityErrorId, securityErrorMessage)
    }

    fun okButton() = R.id.ok_button
}