package com.jorgedguezm.elections.api

import com.jorgedguezm.elections.models.ApiResponse
import com.jorgedguezm.elections.models.Election

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    suspend fun getElections(
        @Query("place") place: String,
        @Query("chamberName") chamberName: String? = null
    ): ApiResponse<List<Election>>

    @GET("elections/{id}")
    suspend fun getElection(@Path("id") id: Long): ApiResponse<Election>
}