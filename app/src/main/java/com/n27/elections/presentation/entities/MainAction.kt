package com.n27.elections.presentation.entities

internal sealed class MainAction {

    object ShowDisclaimer: MainAction()
    data class ShowErrorSnackbar(val error: String?) : MainAction()
}
