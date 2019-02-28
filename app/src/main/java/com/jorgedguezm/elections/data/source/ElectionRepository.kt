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
        return if (utils.isConnectedToInternet()) getElectionsFromApi()
        else getElectionsFromDb()
    }

    fun getElectionsFromApi(): Observable<List<Election>> {
        return apiInterface.getElections()
                .doOnNext {
                    electionsDao.deleteAll()

                    for (item in it)
                        electionsDao.insertElection(item)
                }
    }

    fun getElectionsFromDb(): Observable<List<Election>> {
        return electionsDao.queryElections().toObservable()
    }
}