package com.n27.regional_live.locals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.regional_live.locals.LocalsState.ElectionResult
import com.n27.regional_live.locals.LocalsState.Failure
import com.n27.regional_live.locals.LocalsState.Loading
import com.n27.regional_live.locals.LocalsState.Regions
import com.n27.regional_live.locals.comm.LocalsEvent
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val repository: LiveRepository,
    eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableStateFlow<LocalsState>(Loading)
    internal val viewState = state.asStateFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        state.tryEmit(Failure(throwable.message))
    }

    init {
        eventBus.event
            .onEach(::onEvent)
            .launchIn(viewModelScope)
    }

    private suspend fun onEvent(event: LocalsEvent) {
        when (event) {
            is RequestElection -> requestElection(event.ids)
            is ShowError -> state.emit(Failure(event.error))
        }
    }

    internal fun requestRegions() {
        viewModelScope.launch(exceptionHandler) {
            val regions = repository.getRegions()
            val stateResult = regions?.let { Regions(it.regions) } ?: Failure()
            state.emit(stateResult)
        }
    }

    private fun requestElection(ids: LocalElectionIds) {
        viewModelScope.launch(exceptionHandler) {
            val elections = repository.getLocalElection(ids)
            state.emit(ElectionResult(elections, ids))
        }
    }
}
