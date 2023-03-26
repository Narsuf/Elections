package com.n27.core.data

import android.app.Application
import android.content.Context
import android.util.Log
import com.n27.core.data.api.ElPaisApi
import com.n27.core.data.api.ElectionXml
import com.n27.core.data.api.toElectionXml
import com.n27.core.data.room.ElectionDao
import com.n27.core.extensions.toStringId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.buffer
import okio.source
import org.intellij.lang.annotations.Language
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.nio.charset.StandardCharsets.UTF_8
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(
    private val service: ElPaisApi,
    private val dao: ElectionDao
) {

    suspend fun getRegionalElections(): List<ElectionXml> {
        //val elections = mutableListOf<ElectionXml>()
        val regions = JSONObject(loadJSON("regions.json"))
            .getJSONArray("regions")
        val provinceRegions = JSONObject(loadJSON("provinces.json"))
            .getJSONArray("provinces")

        for (z in 0 until 1) {
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

    suspend fun loadJSON(res: String) = withContext(Dispatchers.IO) {
        runCatching {
            val inputStream = javaClass.classLoader!!.getResourceAsStream(res)
            val buffer = inputStream.source().buffer()
            buffer.readString(UTF_8).apply { inputStream.close() }
        }.getOrElse { "" }
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
