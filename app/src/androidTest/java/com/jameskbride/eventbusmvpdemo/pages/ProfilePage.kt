package com.jameskbride.eventbusmvpdemo.pages

import clickOn
import com.jameskbride.eventbusmvpdemo.R
import shouldScrollToAndDisplay
import typeInto

class ProfilePage {

    fun enterProfileId(profileId: String) {
        typeInto(profileEditButton(), profileId)
    }

    fun profileEditButton() = R.id.profile_id_edit
    fun customerName() = R.id.customer_name
    fun addressLine1() = R.id.address_line_1
    fun addressLine2() = R.id.address_line_2
    fun city() = R.id.city
    fun state() = R.id.state
    fun zip() = R.id.zipcode

    fun submitProfileId() {
        clickOn(R.id.submit)
    }

    fun displaysOrderText(text: String) {
        shouldScrollToAndDisplay(text)
    }
}