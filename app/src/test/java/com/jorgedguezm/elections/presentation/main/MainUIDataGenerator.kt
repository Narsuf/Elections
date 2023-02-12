package com.jorgedguezm.elections.presentation.main

import com.jorgedguezm.elections.data.utils.ElectionGenerator.Companion.generateElections
import com.jorgedguezm.elections.presentation.main.entities.MainState.Error
import com.jorgedguezm.elections.presentation.main.entities.MainState.Success

fun getMainSuccess() = Success(
    elections = generateElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: String?) = Error(error)
