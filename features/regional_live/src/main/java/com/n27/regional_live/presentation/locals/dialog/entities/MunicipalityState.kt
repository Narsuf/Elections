package com.n27.regional_live.presentation.locals.dialog.entities

import com.n27.core.domain.region.models.Province

sealed class MunicipalityState {

    object Empty : MunicipalityState()
    data class Content(val provinces: List<Province>) : MunicipalityState()
}
