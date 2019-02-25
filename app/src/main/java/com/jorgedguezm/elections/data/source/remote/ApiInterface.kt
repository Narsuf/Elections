package com.jorgedguezm.elections.data.source.remote

import com.jorgedguezm.elections.data.Election

import io.reactivex.Observable

import retrofit2.http.GET

interface ApiInterface {

    @GET("elections")
    fun getElections(): Observable<List<Election>>
}