package com.n27.regional_live.locals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.locals.comm.LocalsEvent
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.locals.entities.LocalsAction
import com.n27.regional_live.locals.entities.LocalsAction.NavigateToDetail
import com.n27.regional_live.locals.entities.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.locals.entities.LocalsState
import com.n27.regional_live.locals.entities.LocalsState.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val repository: LiveRepository,
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
        viewModelScope.launchCatching(::errorState) {
            val regions = repository.getRegions()
            val stateResult = regions?.let { Content(it.regions) } ?: Error()
            state.emit(stateResult)
        }
    }

    private suspend fun errorState(throwable: Throwable) { state.emit(Error(throwable.message)) }

    private suspend fun onEvent(event: LocalsEvent) {
        when (event) {
            is RequestElection -> requestElection(event.ids)
            is ShowError -> state.emit(Error(event.error))
        }
    }

    private fun requestElection(ids: LocalElectionIds) {
        viewModelScope.launchCatching(::errorAction) {
            val elections = repository.getLocalElection(ids)
            action.send(NavigateToDetail(elections, ids))
        }
    }

    private suspend fun errorAction(throwable: Throwable) {
        action.send(ShowErrorSnackbar(throwable.message))
    }
}
