package com.n27.regional_live.ui.regional_live.regionals

import com.n27.core.data.models.Election

sealed class RegionalsState {

    object Loading : RegionalsState()
    data class Success(val elections: List<Election>) : RegionalsState()
    data class Failure(val throwable: Throwable? = null) : RegionalsState()
}