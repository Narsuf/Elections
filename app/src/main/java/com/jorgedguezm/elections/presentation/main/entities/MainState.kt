package com.jorgedguezm.elections.presentation.main.entities

import com.jorgedguezm.elections.data.models.Election

typealias OnElectionClicked = (congressElection: Election, senateElection: Election) -> Unit

sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()

    data class Success(val elections: List<Election>, val onElectionClicked: OnElectionClicked) : MainState()
    data class Error(val throwable: Throwable) : MainState()
}