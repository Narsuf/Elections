package com.n27.elections.presentation

import com.n27.elections.presentation.entities.MainState.Content
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.test.generators.getElectionList

fun getMainContent() = Content(getElectionList(), getElectionList())

fun getMainError(error: String? = null) = Error(error)
