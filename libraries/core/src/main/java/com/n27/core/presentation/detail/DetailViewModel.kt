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

    private val state = MutableStateFlow<DetailState>(Loading())
    internal val viewState = state.asStateFlow()
    private var lastState: DetailState = Loading()

    private val action = Channel<DetailAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        lastState = state.value

        viewModelScope.launchCatching(::handleError) {
            val isAnimation = lastState !is Content
            state.emit(Loading(isAnimation))

            val result = when {
                localElectionIds != null -> repository.getLocalElection(localElectionIds).toContent()
                electionId != null -> repository.getRegionalElection(electionId).toContent()
                election != null -> election.toContent()
                else -> null
            }

            result?.let {
                contentState.emit(it)
                state.emit(Content)
            } ?: handleError()
        }
    }

    private suspend fun handleError(throwable: Throwable? = null) {
        if (lastState is Content) {
            action.send(ShowErrorSnackbar(throwable?.message))
            state.emit(Content)
        } else {
            state.emit(Error(throwable?.message))
        }
    }
}
