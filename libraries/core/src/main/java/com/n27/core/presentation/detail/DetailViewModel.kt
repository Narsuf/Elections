package com.n27.core.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.extensions.launchCatching
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailContentState
import com.n27.core.presentation.detail.models.DetailContentState.Empty
import com.n27.core.presentation.detail.models.DetailContentState.WithData
import com.n27.core.presentation.detail.models.DetailState
import com.n27.core.presentation.detail.models.DetailState.Content
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: LiveRepositoryImpl,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

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

            when {
                localElectionIds != null -> repository.getLocalElection(localElectionIds).handleResult()
                electionId != null -> repository.getRegionalElection(electionId).collect { it.handleResult() }
                election != null -> emitContent(election.toContent())
                else -> handleError()
            }
        }
    }

    private suspend fun Result<LiveElection>.handleResult() {
        onSuccess { emitContent(it.election.toContent()) }
        onFailure { handleError() }
    }

    private suspend fun emitContent(withData: WithData) {
        contentState.emit(withData)
        state.emit(Content)
    }

    private suspend fun handleError(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }

        if (lastState is Content) {
            action.send(ShowErrorSnackbar(throwable?.message))
            state.emit(Content)
        } else {
            state.emit(Error(throwable?.message))
        }
    }
}
