package com.narsuf.elections.presentation.main.entities

import com.narsuf.elections.data.models.Election

internal sealed class MainEvent {

    data class NavigateToDetail(val congressElection: Election, val senateElection: Election) : MainEvent()
}
