package com.n27.regional_live.regionals.models

sealed class RegionalsState {

    object Loading : RegionalsState()
    object Content : RegionalsState()
    data class Error(val error: String? = null) : RegionalsState()
}
