package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.ApiResponse
import com.jorgedguezm.elections.data.models.Election

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ElectionApi {

    @GET("election")
    suspend fun getElections(): ApiResponse

    @GET("election/{id}")
    suspend fun getElection(@Path("id") id: Long): Election
}
