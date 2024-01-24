package com.n27.core.domain

import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.RegionRepository
import com.n27.core.domain.region.models.Municipality
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Regions

class LiveUseCase(private val liveRepository: LiveRepository, private val regionalRepository: RegionRepository) {

    suspend fun getCongressElection(): Result<LiveElection> = liveRepository.getCongressElection()

    suspend fun getSenateElection(): Result<LiveElection> = liveRepository.getSenateElection()

    suspend fun getRegionalElections(): Result<LiveElections> = getRegions().mapCatching {
        liveRepository.getRegionalElections(it).getOrThrow()
    }

    suspend fun getRegionalElection(id: String): Result<LiveElection> = getRegions().mapCatching {
        liveRepository.getRegionalElection(id, it).getOrThrow()
    }

    suspend fun getLocalElection(localElectionIds: LocalElectionIds): Result<LiveElection> = getRegions().mapCatching {
        val municipalityName = regionalRepository.getMunicipalityName(it, localElectionIds)
        liveRepository.getLocalElection(localElectionIds, municipalityName).getOrThrow()
    }

    suspend fun getRegions(): Result<Regions> = regionalRepository.getRegions()
    suspend fun getProvinces(region: String): List<Province> = regionalRepository.getProvinces(region)
    suspend fun getMunicipalities(province: String): List<Municipality> = regionalRepository
        .getMunicipalities(province)
        .sortedBy { it.name }
}