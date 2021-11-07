package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.ApiResponse
import com.jorgedguezm.elections.room.ElectionsDao
import com.jorgedguezm.elections.utils.Utils

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(private val service: ApiInterface,
                                             private val dao: ElectionsDao,
                                             val utils: Utils) {

    fun loadElections(place: String, chamber: String?): Observable<ApiResponse> {
        return Observable.concatArrayEager(
            // get items from db first
            getElectionsFromDb(place, chamber).subscribeOn(Schedulers.io()),
            // get items from api if Network is Available
            Observable.defer {
                if (utils.isConnectedToInternet()) {
                    // get new items from api
                    getElectionsFromApi(place, chamber).subscribeOn(Schedulers.io()).doOnNext {
                        dao.insertElections(it.elections)
                    }
                } else {
                    // or return empty
                    Observable.empty()
                }
            }.subscribeOn(Schedulers.io())
        )
    }

    private fun getElectionsFromDb(place: String, chamber: String?): Observable<ApiResponse> {
        val elections = if (chamber != null)
            dao.queryChamberElections(place, chamber)
        else
            dao.queryElections(place)

        return Observable.just(ApiResponse(elections))
    }

    private fun getElectionsFromApi(place: String, chamber: String?): Observable<ApiResponse> {
        return if (chamber != null)
            service.getChamberElections(place, chamber)
        else
            service.getElections(place)
    }
}