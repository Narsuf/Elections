package com.n27.core.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.domain.election.models.Election
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction
import com.n27.core.presentation.detail.models.DetailAction.Refresh
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailState
import com.n27.core.presentation.detail.models.DetailState.Content
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: LiveRepositoryImpl,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

    private val state = MutableLiveData<DetailState>(Loading)
    internal val viewState: LiveData<DetailState> = state

    private val action = MutableLiveData<DetailAction>()
    internal val viewAction: LiveData<DetailAction> = action

    internal fun requestElection(
        election: Election?,
        electionId: String?,
        localElectionIds: LocalElectionIds?
    ) {
        viewModelScope.launchCatching(::handleError) {
            if (state.value is Content) action.value = Refresh

            when {
                localElectionIds != null -> repository.getLocalElection(localElectionIds).handleFlow()
                electionId != null -> repository.getRegionalElection(electionId).handleFlow()
                election != null -> state.value = election.toContent()
                else -> handleError()
            }
        }
    }

    private suspend fun Flow<Result<LiveElection>>.handleFlow() {
        collect { result ->
            result
                .onSuccess { state.value = it.election.toContent() }
                .onFailure { handleError() }
        }
    }

    private suspend fun handleError(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }

        if (state.value is Content)
            action.value = ShowErrorSnackbar(throwable?.message)
        else
            state.value = Error(throwable?.message)

    }
}
