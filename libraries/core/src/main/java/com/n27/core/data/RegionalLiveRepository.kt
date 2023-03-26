package com.n27.core.data

import android.util.Log
import com.n27.core.data.api.ElPaisApi
import com.n27.core.data.api.ElectionXml
import com.n27.core.data.api.toElectionXml
import com.n27.core.data.json.JsonReader
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

    suspend fun getLocalElections(): List<ElectionXml> {
        //val elections = mutableListOf<ElectionXml>()
        val regions = JSONObject(jsonReader.getStringJsonFromResource(res = "regions.json"))
            .getJSONArray("regions")
        val provinceRegions = JSONObject(jsonReader.getStringJsonFromResource(res = "provinces.json"))
            .getJSONArray("provinces")

        for (z in 18 until provinceRegions.length()) {
            val region = provinceRegions.getJSONObject(z)
            val provinces = region.getJSONArray(region.names()!!.getString(0))
            val regionId = regions.getJSONObject(z).getString("id")

            for (y in 0 until provinces.length()) {
                val province = provinces.getJSONObject(y)
                val provinceId = province.getString("id")
                val provinceName = province.getString("name")
                val provinceJson = """
                          {"$provinceName": [
                        """.trimIndent()
                Log.e("json", provinceJson)

                for (x in 0..2000) {
                    getLocalElection(regionId, provinceId, x.toStringId())?.let {
                        val election = it.toElectionXml(x.toStringId())

                        @Language("json")
                        val json = """
                          {
                            "id": "${x.toStringId()}",
                            "name": "${election.place}"
                          },
                        """.trimIndent()
                        Log.e("json", json)
                    }
                }

                val provinceCloseJson = """
                          ]},
                        """.trimIndent()
                Log.e("json", provinceCloseJson)
            }
        }

        Log.e("json", "end")
        return emptyList()
    }

    suspend fun getRegionalElection(id: String) = service.getRegionalElection(id)
    suspend fun getLocalAutonomy(regionId: String) = service.getLocalAutonomy(regionId)
    suspend fun getLocalProvince(regionId: String, provinceId:String) = service.getLocalProvince(regionId, provinceId)
    suspend fun getLocalElection(
        regionId: String,
        provinceId:String,
        municipalityId: String
    ) = service.getLocalElection(regionId, provinceId, municipalityId)
    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }
}
