package com.n27.core.data

import com.n27.core.Constants
import com.n27.core.data.api.ElPaisApi
import com.n27.core.data.api.models.ElectionXml
import com.n27.core.data.api.mappers.toElectionXml
import com.n27.core.data.common.DataUtils
import com.n27.core.data.json.JsonReader
import com.n27.core.data.json.mappers.toMunicipalities
import com.n27.core.data.json.mappers.toProvinces
import com.n27.core.data.json.models.Municipality
import com.n27.core.data.json.models.Province
import com.n27.core.data.json.models.Regions
import com.n27.core.data.room.ElectionDao
import com.n27.core.extensions.lowercaseNames
import com.n27.core.extensions.toStringId
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LiveRepository @Inject constructor(
    private val service: ElPaisApi,
    private val dao: ElectionDao,
    private val jsonReader: JsonReader,
    private val moshi: Moshi,
    private val utils: DataUtils
) {

    suspend fun getRegionalElections(): List<ElectionXml> {
        if (!utils.isConnectedToInternet()) throw Throwable(Constants.NO_INTERNET_CONNECTION)

        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            getRegionalElection(i.toStringId())?.let {
                elections.add(it.toElectionXml(i.toStringId()))
            }
        }

        return elections
    }

    suspend fun getRegions(): Regions? {
        val jsonString = jsonReader.getStringJson(res = "regions.json")
        val adapter: JsonAdapter<Regions> = moshi.adapter(Regions::class.java)
        return adapter.fromJson(jsonString)
    }

    suspend fun getProvinces(region: String): List<Province> {
        val jsonString = jsonReader.getStringJson(res = "provinces.json")
        val jsonRegions = JSONObject(jsonString).getJSONArray("regions")
        return jsonRegions.toProvinces(region)
    }

    suspend fun getLocalMunicipalities(province: String): List<Municipality> {
        val jsonString = jsonReader.getStringJson(res = "municipalities.json")
        val jsonProvinces = JSONObject(jsonString).getJSONArray("provinces")
        return jsonProvinces.toMunicipalities(province)
    }

    suspend fun getRegionalElection(id: String) = service.getRegionalElection(id)

    suspend fun getLocalElection(
        regionId: String,
        provinceId:String,
        municipalityId: String
    ) = service.getLocalElection(regionId, provinceId, municipalityId)

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties().lowercaseNames() }
}
