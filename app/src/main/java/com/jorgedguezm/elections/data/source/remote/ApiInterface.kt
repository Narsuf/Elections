package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party

import io.reactivex.Observable

import retrofit2.http.GET

interface ApiInterface {

    @GET("elections")
    fun getElections(): Observable<List<Election>>

    @GET("parties")
    fun getParties(): Observable<List<Party>>
}