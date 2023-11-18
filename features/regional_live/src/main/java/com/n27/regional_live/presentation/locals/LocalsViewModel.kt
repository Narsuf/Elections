package com.n27.regional_live.presentation.locals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.regional_live.presentation.locals.comm.LocalsEvent
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.entities.LocalsAction
import com.n27.regional_live.presentation.locals.entities.LocalsAction.NavigateToDetail
import com.n27.regional_live.presentation.locals.entities.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.entities.LocalsState
import com.n27.regional_live.presentation.locals.entities.LocalsState.Content
import com.n27.regional_live.presentation.locals.entities.LocalsState.Error
import com.n27.regional_live.presentation.locals.entities.LocalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val useCase: LiveUseCase,
    private val crashlytics: FirebaseCrashlytics?,
    eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableStateFlow<LocalsState>(Loading)
    internal val viewState = state.asStateFlow()

    private val action = Channel<LocalsAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    init {
        eventBus.event
            .onEach(::onEvent)
            .launchIn(viewModelScope)
    }

    internal fun requestRegions() {
        viewModelScope.launch {
            useCase.getRegions()
                .onSuccess { state.emit(Content(it.regions)) }
                .onFailure {
                    crashlytics?.recordException(it)
                    state.emit(Error(it.message))
                }
        }
    }

    private suspend fun onEvent(event: LocalsEvent) {
        when (event) {
            is RequestElection -> requestElection(event.ids)
            is ShowError -> handleError(event.throwable)
        }
    }

    private fun requestElection(ids: LocalElectionIds) {
        viewModelScope.launch {
            useCase.getLocalElection(ids).collect { result ->
                result
                    .onFailure { handleError(it) }
                    .onSuccess { action.send(NavigateToDetail(ids)) }
            }
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        crashlytics?.recordException(throwable)
        action.send(ShowErrorSnackbar(throwable.message))
    }
}
