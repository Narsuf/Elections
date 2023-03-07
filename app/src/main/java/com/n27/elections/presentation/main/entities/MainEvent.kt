package com.n27.elections.presentation.main.entities

import com.n27.core.data.models.Election

internal sealed class MainEvent {

    object ShowDisclaimer: MainEvent()
    object NavigateToLive: MainEvent()
    data class NavigateToDetail(val congressElection: Election, val senateElection: Election) : MainEvent()
}
