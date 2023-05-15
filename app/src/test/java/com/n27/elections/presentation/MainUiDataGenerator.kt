package com.n27.elections.presentation

import com.n27.elections.presentation.models.MainContentState.WithData
import com.n27.elections.presentation.models.MainState.Error
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getElections

fun getMainContent() = WithData(getElectionList())

fun getMainError(error: String? = null) = Error(error)
