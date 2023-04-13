package com.n27.regional_live.regionals.models

import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.remote.api.models.ElectionXml

sealed class RegionalsContentState {

    object Empty : RegionalsContentState()
    data class WithData(val elections: List<ElectionXml>, val parties: List<PartyRaw>) : RegionalsContentState()
}
