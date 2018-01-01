package com.jameskbride.eventbusmvpdemo.bus

import com.jameskbride.eventbusmvpdemo.network.ProfileResponse

data class GetProfileResponseEvent constructor(val profileResponse: ProfileResponse)