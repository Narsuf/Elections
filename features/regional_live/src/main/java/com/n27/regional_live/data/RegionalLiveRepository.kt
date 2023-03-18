package com.n27.regional_live.data

import com.n27.core.data.models.Election
import com.n27.core.extensions.toStringYear
import com.n27.regional_live.data.api.ElPaisApi
import com.n27.regional_live.data.api.mappers.toElection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(private val service: ElPaisApi) {

    suspend fun getRegionalElections(year: Int): List<Election> {
        val elections = mutableListOf<Election>()

        for (i in 1..17) {

            service.getRegionalElection(year, i.toStringYear())?.let {
                elections.add(it.toElection())
            }
        }

        return elections
    }
}
