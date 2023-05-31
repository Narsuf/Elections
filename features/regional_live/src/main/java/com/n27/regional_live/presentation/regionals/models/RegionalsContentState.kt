package com.n27.regional_live.presentation.regionals.models

import com.n27.core.domain.live.models.LiveElections

sealed class RegionalsContentState {

    object Empty : RegionalsContentState()
    data class WithData(val elections: LiveElections) : RegionalsContentState()
}
