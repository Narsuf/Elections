package com.n27.elections.presentation.models

import com.n27.core.data.models.Election

sealed class MainContentState {

    object Empty : MainContentState()
    data class WithData(val elections: List<Election>) : MainContentState()
}
