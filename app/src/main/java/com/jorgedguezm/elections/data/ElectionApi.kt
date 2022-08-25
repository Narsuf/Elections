package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.ApiResponse
import com.jorgedguezm.elections.data.models.Election

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ElectionApi {

    @GET("elections")
    suspend fun getElections(
        @Query("place") place: String,
        @Query("chamberName") chamberName: String?
    ): ApiResponse<List<Election>>

    @GET("elections/{id}")
    suspend fun getElection(@Path("id") id: Long): ApiResponse<Election>
}
