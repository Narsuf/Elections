package com.n27.elections.data.api

import com.n27.core.domain.models.Election
import com.n27.elections.data.api.models.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ElectionApi {

    @GET("election")
    suspend fun getElections(): ApiResponse

    @GET("election/{id}")
    suspend fun getElection(@Path("id") id: Long): Election
}
