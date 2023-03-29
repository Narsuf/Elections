package com.n27.regional_live.ui.regional_live.locals

import com.n27.core.data.json.models.Region
sealed class LocalsState {

    object Loading : LocalsState()
    data class Success(val regions: List<Region>) : LocalsState()
    data class Failure(val throwable: Throwable? = null) : LocalsState()
}
