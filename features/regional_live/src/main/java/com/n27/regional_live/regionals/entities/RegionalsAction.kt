package com.n27.regional_live.regionals.entities

internal sealed class RegionalsAction {

    data class ShowErrorSnackbar(val error: String?) : RegionalsAction()
}