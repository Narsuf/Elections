package com.n27.core.domain.region

import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Municipality
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Regions

interface RegionRepository {

    suspend fun getRegions(): Result<Regions>
    suspend fun getProvinces(region: String): List<Province>
    suspend fun getMunicipalities(province: String): List<Municipality>
    suspend fun getMunicipalityName(regions: Regions, ids: LocalElectionIds): String
}