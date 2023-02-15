package com.jorgedguezm.elections.presentation.main

import com.jorgedguezm.elections.data.utils.getElections
import com.jorgedguezm.elections.presentation.common.Errors
import com.jorgedguezm.elections.presentation.common.Errors.UNKNOWN
import com.jorgedguezm.elections.presentation.main.entities.MainState.Error
import com.jorgedguezm.elections.presentation.main.entities.MainState.Success

fun getMainSuccess() = Success(
    elections = getElections(),
    onElectionClicked = { _, _ ->}
)

fun getMainError(error: Errors = UNKNOWN) = Error(error)
