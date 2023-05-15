package com.n27.regional_live.data

import com.n27.core.data.local.json.JsonReader
import com.n27.regional_live.data.mappers.toMunicipalities
import com.n27.regional_live.data.mappers.toProvinces
import com.n27.regional_live.domain.RegionRepository
import com.n27.regional_live.domain.models.Municipality
import com.n27.regional_live.domain.models.Province
import com.n27.regional_live.domain.models.Regions
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class RegionRepositoryImpl @Inject constructor(
    private val jsonReader: JsonReader,
    private val moshi: Moshi
) : RegionRepository {

    override suspend fun getRegions(): Result<Regions> {
        val jsonString = jsonReader.getStringJson(res = "regions.json")
        val adapter: JsonAdapter<Regions> = moshi.adapter(Regions::class.java)
        return adapter.fromJson(jsonString)
            ?.let { success(it) }
            ?: failure(Throwable("Error reading regions.json"))
    }

    override suspend fun getProvinces(region: String): List<Province> = jsonReader
        .getStringJson(res = "provinces.json")
        .toProvinces(region)

    override suspend fun getMunicipalities(province: String): List<Municipality> = jsonReader
        .getStringJson(res = "municipalities.json")
        .toMunicipalities(province)
}