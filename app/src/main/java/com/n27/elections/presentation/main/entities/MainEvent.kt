package com.n27.elections.presentation.main.entities

import com.n27.elections.data.models.Election

internal sealed class MainEvent {

    data class NavigateToDetail(val congressElection: Election, val senateElection: Election) : MainEvent()
}
