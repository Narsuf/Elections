package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election

import io.reactivex.Observable

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {

    @GET("elections")
    fun getElections(@Query("place") place: String): Observable<List<Election>>
}