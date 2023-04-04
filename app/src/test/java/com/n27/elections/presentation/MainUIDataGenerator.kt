package com.n27.elections.presentation

import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Content
import com.n27.test.generators.getElections

fun getMainContent() = Content(getElections())

fun getMainError(error: String? = null) = Error(error)
