package com.n27.elections.presentation.entities

import com.n27.core.domain.election.models.Election

sealed class MainState {

    object Loading : MainState()
    data class Content(val congressElections: List<Election>, val senateElections: List<Election>) : MainState()
    data class Error(val errorMessage: String? = null) : MainState()
}
