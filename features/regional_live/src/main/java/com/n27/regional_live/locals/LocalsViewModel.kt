package com.n27.regional_live.locals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.locals.comm.LocalsEvent
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.locals.models.LocalsAction
import com.n27.regional_live.locals.models.LocalsAction.NavigateToDetail
import com.n27.regional_live.locals.models.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.locals.models.LocalsState
import com.n27.regional_live.locals.models.LocalsState.Content
import com.n27.regional_live.locals.models.LocalsState.Error
import com.n27.regional_live.locals.models.LocalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val repository: LiveRepository,
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
            val regions = repository.getRegions().regions
            state.emit(Content(regions))
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
            repository.getLocalElection(ids)
            action.send(NavigateToDetail(ids))
        }
    }

    private suspend fun errorAction(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }
        action.send(ShowErrorSnackbar(throwable?.message))
    }
}
