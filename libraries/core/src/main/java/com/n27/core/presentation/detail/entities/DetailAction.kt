package com.n27.core.presentation.detail.entities

internal sealed class DetailAction {

    object Refreshing : DetailAction()
    data class ShowErrorSnackbar(val error: String?) : DetailAction()
}
