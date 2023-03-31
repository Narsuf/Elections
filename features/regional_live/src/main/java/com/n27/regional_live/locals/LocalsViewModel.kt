package com.n27.regional_live.locals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalsViewModel @Inject constructor(
    private val repository: LiveRepository,
    eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableLiveData<LocalsState>(Loading)
    internal val viewState: LiveData<LocalsState> = state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        state.value = Failure(throwable.message)
    }

    init {
        eventBus.event
            .onEach(::onEvent)
            .launchIn(viewModelScope)
    }

    private fun onEvent(event: LocalsEvent) {
        when (event) {
            is RequestElection -> requestElection(event.ids)
            is ShowError -> state.value = Failure(event.error)
        }
    }

    internal fun requestRegions() {
        viewModelScope.launch(exceptionHandler) {
            val regions = repository.getRegions()
            state.value = regions?.let { Regions(it.regions) } ?: Failure()
        }
    }

    private fun requestElection(ids: LocalElectionIds) {
        viewModelScope.launch(exceptionHandler) {
            val elections = repository.getLocalElection(ids)
            state.value = ElectionResult(elections, ids)
        }
    }
}
