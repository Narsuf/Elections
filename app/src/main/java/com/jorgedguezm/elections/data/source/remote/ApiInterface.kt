package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.Results

import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("elections/{place}/{chamberName}")
    fun getElections(@Path("place") place: String,
                     @Path("chamberName") chamberName: String): Observable<List<Election>>

    @GET("election/{year}/{place}/{chamberName}")
    fun getElection(@Path("year") year: String,
                    @Path("place") place: String,
                    @Path("chamberName") chamberName: String): Observable<Election>
}