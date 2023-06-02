package com.n27.regional_live.presentation.locals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.presentation.locals.comm.LocalsEvent
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.models.LocalsAction
import com.n27.regional_live.presentation.locals.models.LocalsAction.NavigateToDetail
import com.n27.regional_live.presentation.locals.models.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.models.LocalsState
import com.n27.regional_live.presentation.locals.models.LocalsState.Content
import com.n27.regional_live.presentation.locals.models.LocalsState.Error
import com.n27.regional_live.presentation.locals.models.LocalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val repository: LiveRepositoryImpl,
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
        viewModelScope.launchCatching(::handleError) {
            repository.getRegions()
                .onSuccess { state.emit(Content(it.regions)) }
                .onFailure { handleError(it) }
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        crashlytics?.recordException(throwable)
        state.emit(Error(throwable.message))
    }

    private suspend fun onEvent(event: LocalsEvent) {
        when (event) {
            is RequestElection -> requestElection(event.ids)
            is ShowError -> errorAction()
        }
    }

    private fun requestElection(ids: LocalElectionIds) {
        viewModelScope.launchCatching(::errorAction) {
            repository.getLocalElection(ids).collect { result ->
                result
                    .onFailure { errorAction(it) }
                    .onSuccess { action.send(NavigateToDetail(ids)) }
            }
        }
    }

    private suspend fun errorAction(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }
        action.send(ShowErrorSnackbar(throwable?.message))
    }
}
