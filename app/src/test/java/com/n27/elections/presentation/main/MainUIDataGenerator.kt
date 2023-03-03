package com.n27.elections.presentation.main

import com.n27.test.generators.getElections
import com.n27.elections.presentation.main.entities.MainState.Error
import com.n27.elections.presentation.main.entities.MainState.Success

fun getMainSuccess() = Success(
    elections = getElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: String? = null) = Error(error)
