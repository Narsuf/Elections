package com.n27.regional_live.regionals.models

internal sealed class RegionalsAction {

    data class ShowErrorSnackbar(val error: String?) : RegionalsAction()
}
