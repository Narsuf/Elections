package com.n27.regional_live.ui.regional_live.locals

import com.n27.core.data.api.models.ElectionXml
import com.n27.core.data.json.models.Region
import com.n27.core.data.json.models.Regions
import com.n27.core.data.room.models.PartyRaw
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState

sealed class LocalsState {

    object Loading : LocalsState()
    data class Success(val regions: List<Region>) : LocalsState()
    data class Failure(val throwable: Throwable? = null) : LocalsState()
}
