package com.jorgedguezm.elections.api

import com.jorgedguezm.elections.models.ApiResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    suspend fun getElections(@Query("place") place: String): ApiResponse

    @GET("elections")
    suspend fun getChamberElections(@Query("place") place: String,
                            @Query("chamberName") chamberName: String): ApiResponse

    @GET("elections/{id}")
    suspend fun getElection(@Path("id") id: Long): ApiResponse
}