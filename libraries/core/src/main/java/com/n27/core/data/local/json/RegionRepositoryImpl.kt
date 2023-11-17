package com.n27.core.data.local.json

import com.n27.core.data.local.json.mappers.toMunicipalities
import com.n27.core.data.local.json.mappers.toProvinces
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.RegionRepository
import com.n27.core.domain.region.models.Municipality
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Regions
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure

@Singleton
class RegionRepositoryImpl @Inject constructor(
    private val jsonReader: JsonReader,
    private val moshi: Moshi
) : RegionRepository {

    override suspend fun getMunicipalityName(regions: Regions, ids: LocalElectionIds): String {
        val region = regions.regions.first { it.id == ids.region }.name
        val province = getProvinces(region).first { it.id == ids.province }.name
        return getMunicipalities(province).first { it.id == ids.municipality }.name
    }

    override suspend fun getRegions(): Result<Regions> {
        val jsonString = jsonReader.getStringJson(res = "regions.json")
        val adapter: JsonAdapter<Regions> = moshi.adapter(Regions::class.java)
        return adapter.fromJson(jsonString)
            ?.let { Result.success(it) }
            ?: failure(Throwable("Error reading regions.json"))
    }

    override suspend fun getProvinces(region: String): List<Province> = jsonReader
        .getStringJson(res = "provinces.json")
        .toProvinces(region)

    override suspend fun getMunicipalities(province: String): List<Municipality> = jsonReader
        .getStringJson(res = "municipalities.json")
        .toMunicipalities(province)
}