package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election

import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    fun getElections(@Query("place") place: String): Observable<List<Election>>

    @GET("elections")
    fun getChamberElections(@Query("place") place: String,
                            @Query("chamberName") chamberName: String): Observable<List<Election>>

    @GET("elections/{id}")
    fun getElection(@Path("id") id: Long): Observable<Election>
}