package com.n27.regional_live.data

import com.n27.regional_live.data.api.ElPaisApi
import com.n27.regional_live.data.api.mappers.toElection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(private val service: ElPaisApi) {

    suspend fun getElection() = service.getRegionalElection(2019, "02")?.toElection()
}
