package com.n27.core.data

import android.util.Log
import com.n27.core.data.api.ElPaisApi
import com.n27.core.data.api.ElectionXml
import com.n27.core.data.api.toElectionXml
import com.n27.core.data.json.JsonReader
import com.n27.core.data.json.mappers.toMunicipalities
import com.n27.core.data.json.mappers.toProvinces
import com.n27.core.data.json.models.Municipalities
import com.n27.core.data.json.models.Provinces
import com.n27.core.data.json.models.Regions
import com.n27.core.data.room.ElectionDao
import com.n27.core.extensions.toStringId
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.intellij.lang.annotations.Language
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(
    private val service: ElPaisApi,
    private val dao: ElectionDao,
    private val jsonReader: JsonReader,
    private val moshi: Moshi
) {

    suspend fun getRegionalElections(): List<ElectionXml> {
        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            getRegionalElection(i.toStringId())?.let {
                elections.add(it.toElectionXml(i.toStringId()))
            }
        }

        return elections
    }

    suspend fun getLocalRegions(): Regions? {
        val jsonString = jsonReader.getStringJsonFromResource(res = "regions.json")
        val adapter: JsonAdapter<Regions> = moshi.adapter(Regions::class.java)
        return adapter.fromJson(jsonString)
    }

    suspend fun getLocalProvinces(): Provinces {
        val jsonString = jsonReader.getStringJsonFromResource(res = "provinces.json")
        val jsonRegions = JSONObject(jsonString).getJSONArray("regions")
        return jsonRegions.toProvinces()
    }

    suspend fun getLocalMunicipalities(): Municipalities {
        val jsonString = jsonReader.getStringJsonFromResource(res = "municipalities.json")
        val jsonProvinces = JSONObject(jsonString).getJSONArray("provinces")
        return jsonProvinces.toMunicipalities()
    }

    suspend fun getRegionalElection(id: String) = service.getRegionalElection(id)

    suspend fun getLocalElection(
        regionId: String,
        provinceId:String,
        municipalityId: String
    ) = service.getLocalElection(regionId, provinceId, municipalityId)

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }
}
