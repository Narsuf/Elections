package com.jorgedguezm.elections.api

import androidx.lifecycle.LiveData

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.models.network.ElectionApiResponse

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    fun getElections(
            @Query("place") place: String): LiveData<ApiResponse<ElectionApiResponse>>

    @GET("elections")
    fun getChamberElections(
            @Query("place") place: String,
            @Query("chamberName") chamberName: String
    ): LiveData<ApiResponse<ElectionApiResponse>>

    @GET("elections/{id}")
    fun getElection(@Path("id") id: Long): LiveData<ApiResponse<Election>>
}