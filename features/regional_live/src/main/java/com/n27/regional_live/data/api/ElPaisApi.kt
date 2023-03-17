package com.n27.regional_live.data.api

import com.n27.core.data.models.ElectionXml
import retrofit2.http.GET
import retrofit2.http.Path

interface ElPaisApi {

    @GET("{year}/autonomicas/{id}/index.xml2")
    suspend fun getRegionalElection(@Path("year") year: Int, @Path("id") id: String): ElectionXml

    @GET("{year}/municipales/{id}/index.xml2")
    suspend fun getLocalAutonomy(@Path("year") year: Int, @Path("id") id: String): ElectionXml

    @GET("{year}/municipales/{autonomyId}/{id}.xml2")
    suspend fun getLocalProvince(
        @Path("year") year: Int,
        @Path("autonomyId") autonomyId: String,
        @Path("id") id: String
    ): ElectionXml
}
