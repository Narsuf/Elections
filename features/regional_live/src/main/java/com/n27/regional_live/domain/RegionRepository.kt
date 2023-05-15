package com.n27.regional_live.domain

import com.n27.regional_live.domain.models.Municipality
import com.n27.regional_live.domain.models.Province
import com.n27.regional_live.domain.models.Regions

interface RegionRepository {

    suspend fun getRegions(): Regions
    suspend fun getProvinces(region: String): List<Province>
    suspend fun getMunicipalities(province: String): List<Municipality>
}