package com.n27.elections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.core.domain.election.models.Election
import com.n27.elections.data.AppRepositoryImpl
import com.n27.elections.domain.ElectionUseCase
import com.n27.elections.presentation.entities.MainAction
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.elections.presentation.entities.MainUiState
import com.n27.elections.presentation.entities.MainUiState.HasElections
import com.n27.elections.presentation.entities.MainUiState.NoElections
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private data class MainViewModelState(
    val congressElections: List<Election> = emptyList(),
    val senateElections: List<Election> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) {

    fun toUiState(): MainUiState = if (congressElections.isNotEmpty() && senateElections.isNotEmpty())
        HasElections(congressElections, senateElections, isLoading, error)
    else
        NoElections(isLoading, error)
}

class MainViewModel @Inject constructor(
    private val appRepository: AppRepositoryImpl,
    private val useCase: ElectionUseCase
) : ViewModel() {

    private val viewModelState = MutableStateFlow(MainViewModelState())
    val uiState = viewModelState
        .map(MainViewModelState::toUiState)
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private val action = Channel<MainAction>(capacity = 1, DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    init { requestElections() }

    internal fun requestElections() {
        viewModelScope.launch {
            viewModelState.update { it.copy(isLoading = true, error = null) }

            if (appRepository.isFirstLaunch()) action.send(ShowDisclaimer)

            useCase.getElections().collect { result ->
                result
                    .onSuccess { elections ->
                        viewModelState.update {
                            it.copy(
                                congressElections = elections.congress,
                                senateElections = elections.senate,
                                isLoading = false
                            )
                        }
                    }
                    .onFailure { throwable ->
                        Firebase.crashlytics.recordException(throwable)
                        viewModelState.update { it.copy(isLoading = false, error = throwable.message) }
                    }
            }
        }
    }

    internal fun saveFirstLaunchFlag() { appRepository.saveFirstLaunch() }
}
