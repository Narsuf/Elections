package com.jorgedguezm.elections.presentation.main.entities

import com.jorgedguezm.elections.data.models.Election

internal sealed class MainEvent {

    data class NavigateToDetail(val congressElection: Election, val senateElection: Election) : MainEvent()
}
