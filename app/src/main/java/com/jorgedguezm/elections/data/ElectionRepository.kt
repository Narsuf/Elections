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

    internal suspend fun getElections(): List<Election> {
        return if (utils.isConnectedToInternet()) {
            val elections = service.getElections().elections
            dao.insertElections(elections)
            elections
        } else {
            getElectionsFromDb()
        }
    }

    internal suspend fun getElectionsFromDb(): List<Election> {
        val elections = dao.queryElections()
        if (elections.isEmpty()) throw Exception("Empty database")
        return elections
    }
}
