package com.n27.regional_live.ui.regional_live.locals.dialog

import com.n27.core.data.api.models.LocalElectionIds
import com.n27.core.data.json.models.Municipality
import com.n27.core.data.json.models.Province
import com.n27.core.data.models.Election

sealed class MunicipalityState {

    object Loading : MunicipalityState()
    data class Provinces(val provinces: List<Province>) : MunicipalityState()
    data class Municipalities(val municipalities: List<Municipality>) : MunicipalityState()
    data class Failure(val throwable: Throwable? = null) : MunicipalityState()
}
