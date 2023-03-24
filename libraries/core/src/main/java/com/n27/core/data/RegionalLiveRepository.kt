package com.n27.core.data

import com.n27.core.data.api.ElPaisApi
import com.n27.core.data.api.ElectionXml
import com.n27.core.data.api.toElectionXml
import com.n27.core.data.room.ElectionDao
import com.n27.core.extensions.toStringYear
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(
    private val service: ElPaisApi,
    private val dao: ElectionDao
) {

    suspend fun getRegionalElections(year: Int): List<ElectionXml> {
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            getRegionalElection(year, id = i.toStringYear())?.let {
                elections.add(it.toElectionXml(electionId = i.toStringYear()))
            }
        }

        return elections
    }

    suspend fun getRegionalElection(year: Int, id: String) = service.getRegionalElection(year, id)

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }
}
