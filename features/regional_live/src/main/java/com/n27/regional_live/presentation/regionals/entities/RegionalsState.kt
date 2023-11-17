package com.n27.regional_live.presentation.regionals.entities

import com.n27.core.domain.live.models.LiveElections

sealed class RegionalsState {

    object Loading : RegionalsState()
    data class Content(val elections: LiveElections) : RegionalsState()
    data class Error(val error: String? = null) : RegionalsState()
}
