package com.n27.elections.presentation.models

import com.n27.core.data.models.Election

sealed class MainState {

    object Loading : MainState()
    data class Content(val elections: List<Election>) : MainState()
    data class Error(val errorMessage: String? = null) : MainState()
}
