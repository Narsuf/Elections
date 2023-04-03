package com.n27.regional_live.locals.entities

import com.n27.core.data.local.json.models.Region

sealed class LocalsState {

    object Loading : LocalsState()
    data class Content(val regions: List<Region>) : LocalsState()
    data class Error(val error: String? = null) : LocalsState()
}
