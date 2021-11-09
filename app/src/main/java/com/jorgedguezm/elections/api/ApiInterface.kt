package com.jorgedguezm.elections.api

import com.jorgedguezm.elections.models.ApiResponse

import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    fun getElections(
            @Query("place") place: String): Single<ApiResponse>

    @GET("elections")
    fun getChamberElections(
            @Query("place") place: String,
            @Query("chamberName") chamberName: String
    ): Single<ApiResponse>

    @GET("elections/{id}")
    fun getElection(@Path("id") id: Long): Single<ApiResponse>
}