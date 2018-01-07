package com.jameskbride.eventbusmvpdemo.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BurritosToGoApi {

    @GET("/profile/{profileId}")
    Call<ProfileResponse> getProfile(@Path("profileId") String profileId);
}