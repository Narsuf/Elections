package com.n27.core.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailAction.ShowProgressBar
import com.n27.core.presentation.detail.models.DetailState
import com.n27.core.presentation.detail.models.DetailState.Content
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class DetailViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<DetailState>(Loading)
    internal val viewState = state.asStateFlow()
    private var lastState: DetailState = Loading

    private val action = Channel<DetailAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        lastState = state.value

        viewModelScope.launchCatching(::handleError) {
            if (lastState is Content)
                action.send(ShowProgressBar)
            else
                state.emit(Loading)

            when {
                localElectionIds != null -> state.emit(repository.getLocalElection(localElectionIds).toContent())
                electionId != null -> state.emit(repository.getRegionalElection(electionId).toContent())
                election != null -> state.emit(election.toContent())
                else -> handleError(Throwable())
            }
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        if (lastState is Content)
            action.send(ShowErrorSnackbar(throwable.message))
        else
            state.value = Error(throwable.message)
    }

}
