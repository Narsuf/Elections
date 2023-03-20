package com.n27.regional_live.data

import com.n27.core.data.models.Election
import com.n27.core.data.room.ElectionDao
import com.n27.core.extensions.toStringYear
import com.n27.regional_live.data.api.ElPaisApi
import com.n27.regional_live.data.api.mappers.toElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(
    private val service: ElPaisApi,
    private val dao: ElectionDao
) {

    suspend fun getRegionalElections(year: Int): List<Election> {
        val parties = getParties()
        val elections = mutableListOf<Election>()

        for (i in 1..17) {
            service.getRegionalElection(year, i.toStringYear())?.let {
                elections.add(it.toElection(parties))
            }
        }

        return elections
    }

    private suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }
}
