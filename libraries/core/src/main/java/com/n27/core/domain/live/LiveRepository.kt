package com.n27.core.domain.live

import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections

interface LiveRepository {

    suspend fun getRegionalElections(): Result<LiveElections>
    suspend fun getRegionalElection(id: String): Result<LiveElection>
    suspend fun getLocalElection(ids: LocalElectionIds): Result<LiveElection>
}