package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface

import io.reactivex.Observable

import javax.inject.Inject

class ElectionRepository @Inject constructor(val apiInterface: ApiInterface,
                                             val electionsDao: ElectionsDao) {

    fun getElections(): Observable<List<Election>> {
        val observableFromApi = getElectionsFromApi()
        val observableFromDb = getElectionsFromDb()
        return Observable.concatArrayEager(observableFromApi, observableFromDb)
    }

    fun getElectionsFromApi(): Observable<List<Election>> {
        return apiInterface.getElections()
                .doOnNext {
                    for (item in it) {
                        electionsDao.insertElection(item)
                    }
                }
    }

    fun getElectionsFromDb(): Observable<List<Election>> {
        return electionsDao.queryElections().toObservable()
    }
}