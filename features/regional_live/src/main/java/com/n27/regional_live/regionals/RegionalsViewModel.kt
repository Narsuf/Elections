package com.n27.regional_live.regionals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.regional_live.regionals.RegionalsState.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<RegionalsState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    internal fun requestElections(initialLoading: Boolean = false) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.tryEmit(Failure(throwable))
        }

        viewModelScope.launch(exceptionHandler) {
            if (!initialLoading) state.emit(Loading)
            val elections = repository.getRegionalElections()

            val stateResult = if (elections.isNotEmpty()) {
                Success(elections, repository.getParties())
            } else {
                Failure()
            }

            state.emit(stateResult)
        }
    }
}
