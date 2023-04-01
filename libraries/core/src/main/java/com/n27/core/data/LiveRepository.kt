package com.n27.core.data

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.remote.api.ElPaisApi
import com.n27.core.data.remote.api.mappers.toElection
import com.n27.core.data.remote.api.mappers.toElectionXml
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.json.mappers.toMunicipalities
import com.n27.core.data.local.json.mappers.toProvinces
import com.n27.core.data.local.json.models.Municipality
import com.n27.core.data.local.json.models.Province
import com.n27.core.data.local.json.models.Regions
import com.n27.core.data.models.Election
import com.n27.core.data.local.room.ElectionDao
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

    private fun errorIfNoConnection() {
        if (!utils.isConnectedToInternet()) throw Throwable(NO_INTERNET_CONNECTION)
    }

    suspend fun getRegionalElections(): List<ElectionXml> {
        errorIfNoConnection()

        val elections = mutableListOf<ElectionXml>()

        for (i in 1..17) {
            getRegionalElectionFromApi(i.toStringId())?.let {
                elections.add(it.toElectionXml(i.toStringId()))
            }
        }

        return elections
    }

    suspend fun getRegionalElection(id: String): Election {
        errorIfNoConnection()
        return getRegionalElectionFromApi(id)?.toElection(getParties()) ?: throw Throwable()
    }

    suspend fun getLocalElection(ids: LocalElectionIds): Election {
        errorIfNoConnection()
        return getLocalElectionFromApi(ids)?.toElection(getParties()) ?: throw Throwable()
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

    suspend fun getMunicipalities(province: String): List<Municipality> {
        val jsonString = jsonReader.getStringJson(res = "municipalities.json")
        val jsonProvinces = JSONObject(jsonString).getJSONArray("provinces")
        return jsonProvinces.toMunicipalities(province)
    }

    suspend fun getRegionalElectionFromApi(id: String) = service.getRegionalElection(id)

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties().lowercaseNames() }

    private suspend fun getLocalElectionFromApi(
        ids: LocalElectionIds
    ) = service.getLocalElection(ids)
}