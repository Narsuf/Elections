package com.n27.core.presentation.detail.models

internal sealed class DetailAction {

    data class ShowErrorSnackbar(val error: String?) : DetailAction()
}
