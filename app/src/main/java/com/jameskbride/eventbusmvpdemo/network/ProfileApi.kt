package com.jameskbride.eventbusmvpdemo.network

import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {

    @GET("/profile/{profileId}")
    fun getProfile(@Path("profileId") profileId:String): Observable<ProfileResponse>
}