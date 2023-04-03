package com.n27.core.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.InitialLoading
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<DetailState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    internal fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        viewModelScope.launchCatching(::error) {
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

    private suspend fun error(throwable: Throwable) {
        state.emit(Failure(throwable.message))
    }
}
