package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.json.mappers.toMunicipalities
import com.n27.core.data.local.json.mappers.toProvinces
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toParties
import com.n27.core.data.remote.api.ElPaisApi
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.data.remote.api.mappers.toLiveElections
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.data.remote.eldiario.ElDiarioApi
import com.n27.core.data.remote.eldiario.mappers.toLiveElection
import com.n27.core.data.remote.eldiario.models.ElDiarioResult
import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.Municipality
import com.n27.core.domain.live.models.Province
import com.n27.core.domain.live.models.Regions
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class LiveRepositoryImpl @Inject constructor(
    private val api: ElPaisApi,
    private val elDiarioApi: ElDiarioApi,
    private val dao: ElectionDao,
    private val utils: DataUtils,
    private val jsonReader: JsonReader,
    private val moshi: Moshi
) : LiveRepository {

    private fun <T> noInternet(): Result<T>? = utils
        .takeIf { !it.isConnectedToInternet() }
        ?.run { failure(Throwable(NO_INTERNET_CONNECTION)) }

    override fun getRegionalElections(): Flow<Result<LiveElections>> = flow {
        getRegions()
            .onFailure { emit(failure(it)) }
            .onSuccess { regions ->
                val result = noInternet() ?: elDiarioApi.getRegionalResults()
                    .mapNotNull { it.toLiveElection(regions) }
                    .let {
                        if (it.isNotEmpty())
                            success(LiveElections(it))
                        else
                            failure(Throwable(BAD_RESPONSE))
                    }

                emit(result)
            }
    }

    override fun getRegionalElection(id: String): Flow<Result<LiveElection>> = flow {
        getRegions()
            .onFailure { emit(failure(it)) }
            .onSuccess { regions ->
                val result = noInternet() ?: elDiarioApi.getRegionalResult(id)?.toLiveElection(regions)
                    ?.let { success(it) }
                    ?: failure(Throwable(BAD_RESPONSE))

                emit(result)
            }
    }

    private suspend fun ElDiarioResult.toLiveElection(regions: Regions): LiveElection? =
        elDiarioApi.getRegionalParties(id)?.let { parties ->
            toLiveElection(
                name = "Autonómicas",
                place = regions.regions.first { it.id == id }.name,
                parties
            )
        }

    override suspend fun getLocalElection(ids: LocalElectionIds): Result<LiveElection> = noInternet()
        ?: api.getLocalElection(ids).getResult()

    private suspend fun ElectionXml?.getResult(): Result<LiveElection> = this
        ?.run { success(toLiveElection(getParties())) } ?: failure(Throwable(BAD_RESPONSE))

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }.toParties()

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
