package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.Results

import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("elections")
    fun getElections(): Observable<List<Election>>

    @GET("parties")
    fun getParties(): Observable<List<Party>>

    @GET("results/{year}/{place}/{chamberName}")
    fun getResults(@Path("year") year: String,
                   @Path("place") place: String,
                   @Path("chamberName") chamberName: String): Observable<List<Results>>
}