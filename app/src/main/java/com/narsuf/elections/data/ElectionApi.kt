package com.narsuf.elections.data

import com.narsuf.elections.data.models.ApiResponse
import com.narsuf.elections.data.models.Election
import retrofit2.http.GET
import retrofit2.http.Path

interface ElectionApi {

    @GET("election")
    suspend fun getElections(): ApiResponse

    @GET("election/{id}")
    suspend fun getElection(@Path("id") id: Long): Election
}
