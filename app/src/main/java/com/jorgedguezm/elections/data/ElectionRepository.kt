package com.jorgedguezm.elections.data

import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.data.utils.DataUtils
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElectionRepository @Inject constructor(internal var service: ElectionApi,
                                             internal var dao: ElectionDao,
                                             internal var utils: DataUtils) {

    internal suspend fun getElections(place: String = "España"): List<Election> {
        return if (utils.isConnectedToInternet()) {
            val elections = service.getElections(place).data
            dao.insertElections(elections)
            elections
        } else {
            getElectionsFromDb(place)
        }
    }

    internal suspend fun getElectionsFromDb(place: String = "España"): List<Election> {
        val elections = dao.queryElections(place)
        if (elections.isEmpty()) throw Exception("Empty database")
        return elections
    }
}
