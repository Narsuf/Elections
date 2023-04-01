package com.n27.core.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.presentation.detail.DetailState.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<DetailState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.tryEmit(Failure(throwable.message))
        }

        viewModelScope.launch(exceptionHandler) {
            state.emit(Loading)

            val resultState = when {
                localElectionIds != null -> Success(repository.getLocalElection(localElectionIds))
                electionId != null -> Success(repository.getRegionalElection(electionId))
                election != null -> Success(election)
                else -> Failure()
            }

            state.emit(resultState)
        }
    }
}
