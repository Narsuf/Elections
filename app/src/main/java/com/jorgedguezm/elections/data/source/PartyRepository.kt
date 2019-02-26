package com.jorgedguezm.elections.data.source

import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.source.local.PartiesDao
import com.jorgedguezm.elections.data.source.remote.ApiInterface
import com.jorgedguezm.elections.utils.Utils

import io.reactivex.Observable

import javax.inject.Inject

class PartyRepository @Inject constructor(val apiInterface: ApiInterface,
                                          val partiesDao: PartiesDao, val utils: Utils) {

    fun getParties(): Observable<List<Party>> {
        val observableFromDb = getPartiesFromDb()
        var returnValue = observableFromDb

        if (utils.isConnectedToInternet())
            returnValue = Observable.concatArrayEager(getPartiesFromApi(), observableFromDb)

        return returnValue
    }

    fun getPartiesFromApi(): Observable<List<Party>> {
        return apiInterface.getParties()
                .doOnNext {
                    for (item in it) partiesDao.insertParty(item)
                }
    }

    fun getPartiesFromDb(): Observable<List<Party>> {
        return partiesDao.queryParties().toObservable()
    }
}