package com.n27.regional_live.locals.dialog.models

import com.n27.core.data.local.json.models.Province

sealed class MunicipalityState {

    object Empty : MunicipalityState()
    data class Content(val provinces: List<Province>) : MunicipalityState()
}
