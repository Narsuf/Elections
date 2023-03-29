package com.n27.regional_live.ui.regional_live.locals.dialog

import com.n27.core.data.json.models.Municipality
import com.n27.core.data.json.models.Province

sealed class MunicipalityState {

    object Loading : MunicipalityState()
    data class ProvincesReceived(val provinces: List<Province>) : MunicipalityState()
    data class Success(val municipalities: List<Municipality>) : MunicipalityState()
    data class Failure(val throwable: Throwable? = null) : MunicipalityState()
}
