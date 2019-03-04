package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.data.source.local.ResultsDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.utils.Utils

import io.reactivex.Observable

import javax.inject.Inject

class ResultsRepository @Inject constructor(val apiInterface: ApiInterface,
                                            val resultsDao: ResultsDao, val utils: Utils) {

    fun getResults(year: Int, place: String, chamberName: String): Observable<List<Results>> {
        val observableFromDb = getResultsFromDb(year, place, chamberName)
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet()) {
            returnValue = Observable.concatArrayEager(
                    getResultsFromApi(year, place, chamberName), observableFromDb)
        }

        return returnValue
    }

    fun getResultsFromApi(year: Int, place: String,
                          chamberName: String): Observable<List<Results>> {
        return apiInterface.getResults(year.toString(), place, chamberName)
                .doOnNext {
                    for (item in it)
                        resultsDao.insertResults(item)
                }
    }

    fun getResultsFromDb(year: Int, place: String, chamberName: String): Observable<List<Results>> {
        return resultsDao.getElectionResults(year, place, chamberName).toObservable()
    }
}