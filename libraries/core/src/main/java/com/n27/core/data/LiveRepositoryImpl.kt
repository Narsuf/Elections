package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.json.JsonReader
import com.n27.core.data.local.json.mappers.toMunicipalities
import com.n27.core.data.local.json.mappers.toProvinces
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class LiveRepositoryImpl @Inject constructor(
    private val api: ElDiarioApi,
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
                val result = noInternet() ?: api.getRegionalResults()
                    .mapNotNull { it.toRegionalLiveElection(regions) }
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
                val result = noInternet() ?: api.getRegionalResult(id)?.toRegionalLiveElection(regions)
                    ?.let { success(it) }
                    ?: failure(Throwable(BAD_RESPONSE))

                emit(result)
            }
    }

    private suspend fun ElDiarioResult.toRegionalLiveElection(regions: Regions): LiveElection? =
        api.getRegionalParties(id)?.let { parties ->
            toLiveElection(
                name = "Autonómicas",
                place = regions.regions.first { it.id == id }.name,
                parties
            )
        }

    override fun getLocalElection(ids: LocalElectionIds): Flow<Result<LiveElection>> = flow {
        getRegions()
            .onFailure { emit(failure(it)) }
            .onSuccess { regions ->
                val result = noInternet() ?: api.getLocalResult(ids)?.let { result ->
                    api.getLocalParties()?.let { parties ->
                        result.toLiveElection(
                            name = "Municipales",
                            place = regions.getMunicipalityName(ids),
                            parties
                        )
                    }
                }?.let { success(it) } ?: failure(Throwable(BAD_RESPONSE))

                emit(result)
            }
    }

    private suspend fun Regions.getMunicipalityName(ids: LocalElectionIds): String {
        val region = regions.first { it.id == ids.region }.name
        val province = getProvinces(region).first { it.id == ids.province }.name
        return getMunicipalities(province).first { it.id == ids.municipality }.name
    }

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
