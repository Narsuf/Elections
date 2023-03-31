package com.n27.core.presentation.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.data.models.Election
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.InitialLoading
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableLiveData<DetailState>(InitialLoading)
    internal val viewState = state

    fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable.message)
        }

        viewModelScope.launch(exceptionHandler) {
            state.value = when {
                localElectionIds != null -> Success(repository.getLocalElection(localElectionIds))
                electionId != null -> Success(repository.getRegionalElection(electionId))
                election != null -> Success(election)
                else -> Failure()
            }
        }
    }
}
