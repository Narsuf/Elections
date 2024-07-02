package com.n27.elections.presentation.entities

import com.n27.core.domain.election.models.Election

sealed interface MainUiState {

    val isLoading: Boolean
    val error: String?

    data class HasElections(
        val congressElections: List<Election>,
        val senateElections: List<Election>,
        override val isLoading: Boolean,
        override val error: String?
    ) : MainUiState

    data class NoElections(
        override val isLoading: Boolean,
        override val error: String?
    ) : MainUiState
}