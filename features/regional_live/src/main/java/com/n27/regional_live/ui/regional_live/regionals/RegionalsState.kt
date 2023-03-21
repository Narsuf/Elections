package com.n27.regional_live.ui.regional_live.regionals

import com.n27.core.data.api.ElectionXml
import com.n27.core.data.models.Party
import com.n27.core.data.room.PartyRaw

sealed class RegionalsState {

    object Loading : RegionalsState()
    data class Success(val elections: List<ElectionXml>, val parties: List<PartyRaw>) : RegionalsState()
    data class Failure(val throwable: Throwable? = null) : RegionalsState()
}
