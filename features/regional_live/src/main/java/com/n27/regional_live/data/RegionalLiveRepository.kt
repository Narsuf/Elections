package com.n27.regional_live.data

import com.n27.regional_live.data.api.ElPaisApi
import com.n27.regional_live.data.api.mappers.toElection
import com.n27.regional_live.data.api.models.ElectionXml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.simpleframework.xml.core.Persister
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(private val service: ElPaisApi) {

    suspend fun getElection() = service.getRegionalElection(2019, "02")?.toElection()
}
