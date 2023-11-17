package com.n27.regional_live.presentation.locals.dialog.models

import com.n27.core.domain.region.models.Municipality

internal sealed class MunicipalityAction {

    data class PopulateMunicipalitiesSpinner(val municipalities: List<Municipality>) : MunicipalityAction()
    data class ShowErrorSnackbar(val error: String? = null) : MunicipalityAction()
}
