package com.n27.core.presentation.detail.entities

internal sealed class DetailAction {

    data class ShowErrorSnackbar(val error: String?) : DetailAction()
}