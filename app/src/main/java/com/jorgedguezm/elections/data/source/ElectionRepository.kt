package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.Utils

import io.reactivex.Observable

import javax.inject.Inject

class ElectionRepository @Inject constructor(val apiInterface: ApiInterface,
                                             val electionsDao: ElectionsDao, val utils: Utils) {

    fun getElections(place: String): Observable<List<Election>> {
        val observableFromDb = getElectionsFromDb(place)
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet()) {
            returnValue = Observable.concatArrayEager(getElectionsFromApi(place),
                    observableFromDb)
        }

        return returnValue
    }

    fun getElectionsFromApi(place: String): Observable<List<Election>> {
        return apiInterface.getElections(place)
                .doOnNext {
                    for (item in it) electionsDao.insertElection(item)
                }
    }

    fun getElectionsFromDb(place: String): Observable<List<Election>> {
        return electionsDao.queryElections(place).toObservable()
    }
}