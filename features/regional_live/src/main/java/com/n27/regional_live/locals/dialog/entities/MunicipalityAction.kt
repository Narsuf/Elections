package com.n27.regional_live.locals.dialog.entities

import com.n27.core.data.local.json.models.Municipality

internal sealed class MunicipalityAction {

    data class PopulateMunicipalitiesSpinner(val municipalities: List<Municipality>) : MunicipalityAction()
    data class ShowErrorSnackbar(val error: String? = null) : MunicipalityAction()
}