package com.n27.core.data

import com.n27.core.Constants.BAD_RESPONSE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toParties
import com.n27.core.data.remote.api.ElPaisApi
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.data.remote.api.mappers.toLiveElections
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class LiveRepositoryImpl @Inject constructor(
    private val api: ElPaisApi,
    private val dao: ElectionDao,
    private val utils: DataUtils
) : LiveRepository {

    private fun <T> noInternet(): Result<T>? = utils
        .takeIf { !it.isConnectedToInternet() }
        ?.run { failure(Throwable(NO_INTERNET_CONNECTION)) }

    override suspend fun getRegionalElections(): Result<LiveElections> = noInternet()
        ?: api.getRegionalElections().run {
            if (isNotEmpty())
                success(toLiveElections(getParties()))
            else
                failure(Throwable(BAD_RESPONSE))
        }

    override suspend fun getRegionalElection(id: String): Result<LiveElection> = noInternet()
        ?: api.getRegionalElection(id).getResult()

    override suspend fun getLocalElection(ids: LocalElectionIds): Result<LiveElection> = noInternet()
        ?: api.getLocalElection(ids).getResult()

    private suspend fun ElectionXml?.getResult(): Result<LiveElection> = this
        ?.run { success(toLiveElection(getParties())) } ?: failure(Throwable(BAD_RESPONSE))

    suspend fun getParties() = withContext(Dispatchers.IO) { dao.getParties() }.toParties()
}
