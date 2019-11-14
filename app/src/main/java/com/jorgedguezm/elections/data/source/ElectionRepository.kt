package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.local.ElectionsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.Utils

import io.reactivex.Observable

import javax.inject.Inject

class ElectionRepository @Inject constructor(val apiInterface: ApiInterface,
                                             val electionsDao: ElectionsDao, val utils: Utils) {

    fun getElections(place: String, chamber: String?): Observable<List<Election>> {
        val observableFromDb = getElectionsFromDb(place, chamber)
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet()) {
            returnValue = Observable.concatArrayEager(getElectionsFromApi(place, chamber),
                    observableFromDb)
        }

        return returnValue
    }

    fun getElectionsFromApi(place: String, chamber: String?): Observable<List<Election>> {
        val observableElections = if (chamber != null)
            apiInterface.getChamberElections(place, chamber)
        else
            apiInterface.getElections(place)

        return observableElections.doOnNext {
            for (item in it) electionsDao.insertElection(item)
        }
    }

    fun getElectionsFromDb(place: String, chamber: String?): Observable<List<Election>> {
        val singleElections = if (chamber != null)
            electionsDao.queryChamberElections(place, chamber)
        else
            electionsDao.queryElections(place)

        return singleElections.toObservable()
    }
}