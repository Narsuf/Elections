package com.jorgedguezm.elections.repository

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.utils.Utils

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(internal var service: ApiInterface,
                                             internal var dao: ElectionDao,
                                             internal var utils: Utils) {

    suspend fun loadElections(place: String, chamber: String?): List<Election> {
        return if (utils.isConnectedToInternet()) {
            val elections = service.getElections(place, chamber).data
            dao.insertElections(elections)
            elections
        } else {
            dao.queryElections(place, chamber)
        }
    }
}