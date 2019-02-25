package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.utils.Utils

import io.reactivex.Observable

import javax.inject.Inject

class ElectionRepository @Inject constructor(val apiInterface: ApiInterface,
                                             val electionsDao: ElectionsDao, val utils: Utils) {

    fun getElections(): Observable<List<Election>> {
        val observableFromDb = getElectionsFromDb()
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet())
            returnValue = Observable.concatArrayEager(getElectionsFromApi(), observableFromDb)

        return returnValue
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