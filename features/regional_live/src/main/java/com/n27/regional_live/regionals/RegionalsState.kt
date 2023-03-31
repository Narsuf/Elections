package com.n27.regional_live.regionals

import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.data.local.room.models.PartyRaw

sealed class RegionalsState {

    object InitialLoading : RegionalsState()
    object Loading : RegionalsState()
    data class Success(val elections: List<ElectionXml>, val parties: List<PartyRaw>) : RegionalsState()
    data class Failure(val throwable: Throwable? = null) : RegionalsState()
}
