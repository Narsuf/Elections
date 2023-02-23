package org.n27.elections.presentation.main

import org.n27.elections.data.utils.getElections
import org.n27.elections.presentation.main.entities.MainState.Error
import org.n27.elections.presentation.main.entities.MainState.Success

fun getMainSuccess() = Success(
    elections = getElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: String? = null) = Error(error)
