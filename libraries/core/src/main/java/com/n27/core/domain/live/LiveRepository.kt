package com.n27.core.domain.live

import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Regions
import kotlinx.coroutines.flow.Flow

interface LiveRepository {

    fun getCongressElection(): Flow<Result<LiveElection>>
    fun getSenateElection(): Flow<Result<LiveElection>>
    fun getLocalElection(ids: LocalElectionIds, municipalityName: String): Flow<Result<LiveElection>>
    fun getRegionalElection(id: String, regions: Regions): Flow<Result<LiveElection>>
    suspend fun getRegionalElections(regions: Regions): Result<LiveElections>
}