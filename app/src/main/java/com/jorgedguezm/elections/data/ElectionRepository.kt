package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(internal var service: ElectionApi,
                                             internal var dao: ElectionDao,
                                             internal var utils: DataUtils) {

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
