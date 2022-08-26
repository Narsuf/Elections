package com.jorgedguezm.elections.presentation.main

import com.jorgedguezm.elections.data.models.ElectionGenerator.Companion.generateElections
import com.jorgedguezm.elections.presentation.main.entities.MainState.*

fun getMainSuccess() = Success(
    elections = generateElections(),
    onElectionClicked = { _, _ ->}
)
