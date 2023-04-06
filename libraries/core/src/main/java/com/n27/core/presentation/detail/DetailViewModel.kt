package com.n27.core.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.core.presentation.detail.mappers.toWithData
import com.n27.core.presentation.detail.models.DetailAction
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailAction.ShowProgressBar
import com.n27.core.presentation.detail.models.DetailContentState
import com.n27.core.presentation.detail.models.DetailContentState.Empty
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

    private val contentState = MutableStateFlow<DetailContentState>(Empty)
    internal val viewContentState = contentState.asStateFlow()

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

        viewModelScope.launchCatching(::error) {
            if (lastState is Content)
                action.send(ShowProgressBar)
            else
                state.emit(Loading)

            if (localElectionIds == null && electionId == null && election == null) {
                manageError()
            } else {
                val resultState = when {
                    localElectionIds != null -> repository.getLocalElection(localElectionIds).toWithData()
                    electionId != null -> repository.getRegionalElection(electionId).toWithData()
                    election != null -> election.toWithData()
                    else -> Empty
                }

                contentState.emit(resultState)
                state.emit(Content)
            }
        }
    }

    private suspend fun error(throwable: Throwable) { manageError(throwable.message) }

    private suspend fun manageError(error: String? = null) {
        if (lastState is Content)
            action.send(ShowErrorSnackbar(error))
        else
            state.emit(Error(error))
    }
}
