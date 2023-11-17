package com.n27.core.domain

import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.RegionRepository
import com.n27.core.domain.region.models.Municipality
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Regions
import kotlinx.coroutines.flow.flow
import kotlin.Result.Companion.failure

class LiveUseCase(private val liveRepository: LiveRepository, private val regionalRepository: RegionRepository) {

    fun getCongressElection() = liveRepository.getCongressElection()

    fun getSenateElection() = liveRepository.getSenateElection()

    fun getRegionalElections() = flow<Result<LiveElections>> {
        getRegions()
            .onSuccess { emit(liveRepository.getRegionalElections(it)) }
            .onFailure { emit(failure(it)) }
    }

    fun getRegionalElection(id: String) = flow<Result<LiveElection>> {
        getRegions()
            .onFailure { emit(failure(it)) }
            .onSuccess { regions ->
                liveRepository.getRegionalElection(id, regions).collect { emit(it) }
            }
    }

    fun getLocalElection(localElectionIds: LocalElectionIds)  = flow<Result<LiveElection>> {
        getRegions()
            .onFailure { emit(failure(it)) }
            .onSuccess { regions ->
                val municipalityName = regionalRepository.getMunicipalityName(regions, localElectionIds)
                liveRepository.getLocalElection(localElectionIds, municipalityName).collect { emit(it) }
            }
    }

    suspend fun getRegions(): Result<Regions> = regionalRepository.getRegions()
    suspend fun getProvinces(region: String): List<Province> = regionalRepository.getProvinces(region)
    suspend fun getMunicipalities(province: String): List<Municipality> = regionalRepository
        .getMunicipalities(province)
        .sortedBy { it.name }
}