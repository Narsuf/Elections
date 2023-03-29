package com.n27.elections.presentation.main

import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Success
import com.n27.test.generators.getElections

fun getMainSuccess() = Success(
    elections = getElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: String? = null) = Error(error)
