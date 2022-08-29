package com.jorgedguezm.elections.presentation.main

import com.jorgedguezm.elections.data.utils.ElectionGenerator.Companion.generateElections
import com.jorgedguezm.elections.presentation.main.entities.MainState.*

fun getMainSuccess() = Success(
    elections = generateElections(),
    onElectionClicked = { _, _ ->}
)
