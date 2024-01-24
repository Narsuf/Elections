package com.n27.core.domain.live

import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Regions

interface LiveRepository {

    suspend fun getCongressElection(): Result<LiveElection>
    suspend fun getSenateElection(): Result<LiveElection>
    suspend fun getLocalElection(ids: LocalElectionIds, municipalityName: String): Result<LiveElection>
    suspend fun getRegionalElection(id: String, regions: Regions): Result<LiveElection>
    suspend fun getRegionalElections(regions: Regions): Result<LiveElections>
}