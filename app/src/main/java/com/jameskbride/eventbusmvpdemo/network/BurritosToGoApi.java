package com.jameskbride.eventbusmvpdemo.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface BurritosToGoApi {

    @GET("/profile/{profileId}")
    Observable<ProfileResponse> getProfile(@Path("profileId") String profileId);
}