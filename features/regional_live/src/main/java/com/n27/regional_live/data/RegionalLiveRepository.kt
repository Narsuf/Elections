package com.n27.regional_live.data

import com.n27.regional_live.data.api.ElPaisApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionalLiveRepository @Inject constructor(private val service: ElPaisApi) {

    suspend fun getElection() = withContext(Dispatchers.IO) {
        URL("http://rsl00.epimg.net/elecciones/2019/autonomicas/02/index.xml2").openStream()
    }
}
