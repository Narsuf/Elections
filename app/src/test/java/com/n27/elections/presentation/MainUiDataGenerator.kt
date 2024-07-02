package com.n27.elections.presentation

import com.n27.core.Constants.KEY_SENATE
import com.n27.elections.presentation.entities.MainUiState.HasElections
import com.n27.elections.presentation.entities.MainUiState.NoElections
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList

fun getHasElections(isLoading: Boolean = false, error: String? = null) = HasElections(
    congressElections = getElectionList(),
    senateElections = listOf(getElection(chamberName = KEY_SENATE)),
    isLoading,
    error
)

fun getNoElections(
    isLoading: Boolean = false,
    error: String? = null
) = NoElections(isLoading, error)
