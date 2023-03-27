package com.n27.elections.presentation.entities

import com.n27.core.data.models.Election
import com.n27.core.presentation.common.OnGeneralElectionClicked

sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Success(
        val elections: List<Election>,
        val onElectionClicked: OnGeneralElectionClicked
    ) : MainState()
    data class Error(val errorMessage: String? = null) : MainState()
}
