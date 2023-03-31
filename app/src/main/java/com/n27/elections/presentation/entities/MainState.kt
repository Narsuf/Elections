package com.n27.elections.presentation.entities

import com.n27.core.data.models.Election
import com.n27.elections.presentation.adapters.OnGeneralElectionClicked

sealed class MainState {

    object InitialLoading : MainState()
    object Loading : MainState()
    data class Success(val elections: List<Election>) : MainState()
    data class Error(val errorMessage: String? = null) : MainState()
}
