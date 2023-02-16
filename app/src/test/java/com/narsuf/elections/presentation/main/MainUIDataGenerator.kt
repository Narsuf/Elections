package com.narsuf.elections.presentation.main

import com.narsuf.elections.data.utils.getElections
import com.narsuf.elections.presentation.main.entities.MainState.Error
import com.narsuf.elections.presentation.main.entities.MainState.Success

fun getMainSuccess() = Success(
    elections = getElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: String? = null) = Error(error)
