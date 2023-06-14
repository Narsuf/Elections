package com.n27.core.domain.live

import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.live.models.Municipality
import com.n27.core.domain.live.models.Province
import com.n27.core.domain.live.models.Regions
import kotlinx.coroutines.flow.Flow

interface LiveRepository {

    fun getCongressElection(): Flow<Result<LiveElection>>
    fun getSenateElection(): Flow<Result<LiveElection>>
    fun getRegionalElections(): Flow<Result<LiveElections>>
    fun getRegionalElection(id: String): Flow<Result<LiveElection>>
    fun getLocalElection(ids: LocalElectionIds): Flow<Result<LiveElection>>
    suspend fun getRegions(): Result<Regions>
    suspend fun getProvinces(region: String): List<Province>
    suspend fun getMunicipalities(province: String): List<Municipality>
}