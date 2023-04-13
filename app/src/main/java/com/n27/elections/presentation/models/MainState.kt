package com.n27.elections.presentation.models

sealed class MainState {

    object Loading : MainState()
    object Content : MainState()
    data class Error(val errorMessage: String? = null) : MainState()
}
