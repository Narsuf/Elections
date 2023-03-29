package com.n27.regional_live.ui.regional_live.locals.comm

import com.n27.core.data.json.models.Province


sealed class LocalsEvent {

    data class ShowProvinces(val provinces: List<Province>) : LocalsEvent()
}
