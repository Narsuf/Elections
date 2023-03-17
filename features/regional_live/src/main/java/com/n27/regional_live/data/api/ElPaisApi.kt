package com.n27.regional_live.data.api

import com.n27.regional_live.data.api.models.ElectionXml
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.simpleframework.xml.core.Persister
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ElPaisApi @Inject constructor(private val client: OkHttpClient) {

    private val baseUrl = "http://rsl00.epimg.net/elecciones/"

    suspend fun getRegionalElection(year: Int, id: String) = withContext(Dispatchers.IO) {
        val url = "$baseUrl$year/autonomicas/$id/index.xml2"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Error: $response")
            response.body?.string()
        }
    }

    /*@GET("{year}/municipales/{id}/index.xml2")
    suspend fun getLocalAutonomy(@Path("year") year: Int, @Path("id") id: String): ElectionXml

    @GET("{year}/municipales/{autonomyId}/{id}.xml2")
    suspend fun getLocalProvince(
        @Path("year") year: Int,
        @Path("autonomyId") autonomyId: String,
        @Path("id") id: String
    ): ElectionXml*/
}
