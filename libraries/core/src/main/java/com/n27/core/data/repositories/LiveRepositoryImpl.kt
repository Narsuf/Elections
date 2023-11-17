package com.n27.core.data.repositories

import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_GENERALS
import com.n27.core.Constants.KEY_LOCALS
import com.n27.core.Constants.KEY_REGIONALS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SPAIN
import com.n27.core.Constants.REGIONAL_ELECTION_EMPTY_LIST
import com.n27.core.data.remote.api.ElDiarioApi
import com.n27.core.data.remote.api.mappers.toLiveElection
import com.n27.core.data.remote.api.models.ElDiarioResult
import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LiveElections
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Regions
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class LiveRepositoryImpl @Inject constructor(private val api: ElDiarioApi) : LiveRepository {

    override fun getCongressElection() = flow<Result<LiveElection>> {
        api.getCongressResult()
            .onFailure { emit(failure(it)) }
            .onSuccess { result ->
                api.getCongressParties().map { parties ->
                    result.toLiveElection(
                        name = KEY_GENERALS,
                        place = KEY_SPAIN,
                        parties,
                        chamberName = KEY_CONGRESS
                    )
                }.let { emit(it) }
            }
    }

    override fun getSenateElection() = flow<Result<LiveElection>> {
        api.getSenateResult()
            .onFailure { emit(failure(it)) }
            .onSuccess { result ->
                api.getSenateParties().map { parties ->
                    result.toLiveElection(
                        name = KEY_GENERALS,
                        place = KEY_SPAIN,
                        parties,
                        chamberName = KEY_SENATE
                    )
                }.let { emit(it) }
            }
    }

    override suspend fun getRegionalElections(regions: Regions): Result<LiveElections> =
        api.getRegionalResults()
            .mapNotNull { it.toLiveElection(regions).getOrNull() }
            .let {
                if (it.isNotEmpty())
                    success(LiveElections(it))
                else
                    failure(Throwable(REGIONAL_ELECTION_EMPTY_LIST))
            }

    override fun getRegionalElection(id: String, regions: Regions) = flow<Result<LiveElection>> {
        api.getRegionalResult(id)
            .onSuccess { emit(it.toLiveElection(regions)) }
            .onFailure { emit(failure(it)) }
    }

    private suspend fun ElDiarioResult.toLiveElection(regions: Regions): Result<LiveElection> =
        api.getRegionalParties(id).map { parties ->
            toLiveElection(
                name = KEY_REGIONALS,
                place = regions.regions.first { it.id == id }.name,
                parties
            )
        }

    override fun getLocalElection(
        ids: LocalElectionIds,
        municipalityName: String
    ) = flow<Result<LiveElection>> {
        api.getLocalResult(ids)
            .onFailure { emit(failure(it)) }
            .onSuccess { result ->
                api.getLocalParties().map {
                    result.toLiveElection(
                        name = KEY_LOCALS,
                        place = municipalityName,
                        parties = it
                    )
                }.let { emit(it) }
            }
    }
}