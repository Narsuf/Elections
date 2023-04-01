package com.n27.elections.presentation

import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Success
import com.n27.test.generators.getElections

fun getMainSuccess() = Success(getElections())

fun getMainError(error: String? = null) = Error(error)
