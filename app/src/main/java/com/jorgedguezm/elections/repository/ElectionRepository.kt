package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.utils.Utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class ElectionRepository @Inject constructor(internal var service: ApiInterface,
                                                  internal var dao: ElectionDao,
                                                  internal var utils: Utils) {

    suspend fun loadElections(place: String, chamber: String?): List<Election> {
        return if (utils.isConnectedToInternet()) {
            val elections = getElectionsFromApi(place, chamber).elections
            dao.insertElections(elections)
            elections
        } else {
            getElectionsFromDb(place, chamber)
        }
    }

    private suspend fun getElectionsFromDb(place: String, chamber: String?) =
        dao.queryElections(place, chamber)

    private suspend fun getElectionsFromApi(place: String, chamber: String?) =
        service.getElections(place, chamber)
}