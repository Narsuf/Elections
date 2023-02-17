package com.n27.elections.data

import com.n27.elections.data.models.ApiResponse
import com.n27.elections.data.models.Election
import retrofit2.http.GET
import retrofit2.http.Path

interface ElectionApi {

    @GET("election")
    suspend fun getElections(): ApiResponse

    @GET("election/{id}")
    suspend fun getElection(@Path("id") id: Long): Election
}
