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
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@Singleton
class LiveRepositoryImpl @Inject constructor(private val api: ElDiarioApi) : LiveRepository {

    override suspend fun getCongressElection(): Result<LiveElection> = api.getCongressResult()
        .mapCatching { result ->
            result.toLiveElection(
                name = KEY_GENERALS,
                place = KEY_SPAIN,
                parties = api.getCongressParties().getOrThrow(),
                chamberName = KEY_CONGRESS
            )
        }

    override suspend fun getSenateElection(): Result<LiveElection> = api.getSenateResult()
        .mapCatching { result ->
            result.toLiveElection(
                name = KEY_GENERALS,
                place = KEY_SPAIN,
                parties = api.getSenateParties().getOrThrow(),
                chamberName = KEY_SENATE
            )
        }

    override suspend fun getRegionalElections(regions: Regions): Result<LiveElections> = api.getRegionalResults()
        .map { it.toLiveElection(regions) }
        .let {
            if (it.isNotEmpty())
                success(LiveElections(it))
            else
                failure(Throwable(REGIONAL_ELECTION_EMPTY_LIST))
        }

    override suspend fun getRegionalElection(id: String, regions: Regions): Result<LiveElection> =
        api.getRegionalResult(id).map { it.toLiveElection(regions) }

    private suspend fun ElDiarioResult.toLiveElection(regions: Regions): LiveElection = toLiveElection(
        name = KEY_REGIONALS,
        place = regions.regions.first { it.id == id }.name,
        parties = api.getRegionalParties(id).getOrThrow()
    )

    override suspend fun getLocalElection(
        ids: LocalElectionIds,
        municipalityName: String
    ): Result<LiveElection> = api.getLocalResult(ids).mapCatching { result ->
        result.toLiveElection(
            name = KEY_LOCALS,
            place = municipalityName,
            parties = api.getLocalParties().getOrThrow()
        )
    }
}