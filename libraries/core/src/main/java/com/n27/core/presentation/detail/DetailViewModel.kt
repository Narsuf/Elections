package com.n27.core.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.RegionalLiveRepository
import com.n27.core.data.api.toElection
import com.n27.core.data.api.toElectionXml
import com.n27.core.data.models.Election
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.Success

class DetailViewModel @Inject constructor(
    private val repository: RegionalLiveRepository
) : ViewModel() {

    private val state = MutableLiveData<DetailState>(Loading)
    internal val viewState: LiveData<DetailState> = state

    fun requestElection(election: Election, electionId: String?) {
        state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable.message)
        }

        viewModelScope.launch(exceptionHandler) {
            state.value = electionId?.let { id ->
                repository.getRegionalElection(election.date.toInt(), id)
                    ?.let { Success(it.toElection(repository.getParties())) }
                    ?: Failure()
            } ?: Success(election)
        }
    }
}
