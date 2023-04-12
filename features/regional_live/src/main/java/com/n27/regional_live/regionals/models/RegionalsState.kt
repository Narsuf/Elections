package com.n27.regional_live.regionals.models

import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.remote.api.models.ElectionXml

sealed class RegionalsState {

    object Loading : RegionalsState()
    object Content : RegionalsState()
    data class Error(val error: String? = null) : RegionalsState()
}
