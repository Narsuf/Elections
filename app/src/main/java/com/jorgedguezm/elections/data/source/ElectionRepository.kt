package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.Utils

import io.reactivex.Observable

import javax.inject.Inject

class ElectionRepository @Inject constructor(val apiInterface: ApiInterface,
                                             val electionsDao: ElectionsDao, val utils: Utils) {

    fun getElections(place: String, chamber: String): Observable<List<Election>> {
        val observableFromDb = getElectionsFromDb(place, chamber)
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet()) {
            returnValue = Observable.concatArrayEager(
                    getElectionsFromApi(place, chamber), observableFromDb)
        }

        return returnValue
    }

    private fun getElectionsFromApi(place: String, chamber: String): Observable<List<Election>> {
        return apiInterface.getElections(place, chamber)
                .doOnNext {
                    for (item in it)
                        electionsDao.insertElection(item)
                }
    }

    private fun getElectionsFromDb(place: String, chamber: String): Observable<List<Election>> {
        return electionsDao.queryElections(place, chamber).toObservable()
    }
}