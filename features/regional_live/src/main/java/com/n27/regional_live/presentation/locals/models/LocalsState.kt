package com.n27.regional_live.presentation.locals.models

import com.n27.core.domain.region.models.Region

sealed class LocalsState {

    object Loading : LocalsState()
    data class Content(val regions: List<Region>) : LocalsState()
    data class Error(val error: String? = null) : LocalsState()
}
