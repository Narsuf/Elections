package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.ApiResponse
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.utils.Utils

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(private val service: ApiInterface,
                                             private val dao: ElectionDao, val utils: Utils) {

    fun loadElections(place: String, chamber: String?): Observable<ApiResponse> {
        return if (utils.isConnectedToInternet()) {
            getElectionsFromApi(place, chamber).subscribeOn(Schedulers.io()).doOnNext {
                dao.insertElections(it.elections).subscribe()
            }
        } else {
            getElectionsFromDb(place, chamber).subscribeOn(Schedulers.io()).flatMap {
                Observable.just(ApiResponse(it))
            }
        }
    }

    private fun getElectionsFromDb(place: String, chamber: String?): Observable<List<Election>> {
        val elections = if (chamber != null)
            dao.queryChamberElections(place, chamber)
        else
            dao.queryElections(place)

        return elections.toObservable()
    }

    private fun getElectionsFromApi(place: String, chamber: String?): Observable<ApiResponse> {
        return if (chamber != null)
            service.getChamberElections(place, chamber)
        else
            service.getElections(place)
    }
}