package com.jameskbride.eventbusmvpdemo;

import com.jameskbride.eventbusmvpdemo.network.ProfileResponse;

public class GetProfileResponseEvent {

    private ProfileResponse profileResponse;

    public GetProfileResponseEvent(ProfileResponse profileResponse) {

        this.profileResponse = profileResponse;
    }

    public ProfileResponse getProfileResponse() {
        return profileResponse;
    }
}
