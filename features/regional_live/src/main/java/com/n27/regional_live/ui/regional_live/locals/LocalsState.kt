package com.n27.regional_live.ui.regional_live.locals

import com.n27.core.data.api.models.LocalElectionIds
import com.n27.core.data.json.models.Region
import com.n27.core.data.models.Election

sealed class LocalsState {

    object Loading : LocalsState()
    data class Regions(val regions: List<Region>) : LocalsState()
    data class ElectionResult(val election: Election, val ids: LocalElectionIds) : LocalsState()
    data class Failure(val error: String? = null) : LocalsState()
}
