package com.n27.core.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.data.remote.api.LiveRepositoryImpl
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction
import com.n27.core.presentation.detail.models.DetailAction.Refreshing
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailFlags
import com.n27.core.presentation.detail.models.DetailInteraction
import com.n27.core.presentation.detail.models.DetailInteraction.Refresh
import com.n27.core.presentation.detail.models.DetailInteraction.ScreenOpened
import com.n27.core.presentation.detail.models.DetailInteraction.Swap
import com.n27.core.presentation.detail.models.DetailState
import com.n27.core.presentation.detail.models.DetailState.Content
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val useCase: LiveUseCase,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

    private val state = MutableLiveData<DetailState>(Loading)
    internal val viewState: LiveData<DetailState> = state

    private val action = MutableLiveData<DetailAction>()
    internal val viewAction: LiveData<DetailAction> = action

    internal fun handleInteraction(interaction: DetailInteraction) = when (interaction) {
        is ScreenOpened -> requestElection(interaction.flags)
        is Refresh -> refresh(interaction)
        is Swap -> swap(interaction)
    }

    private fun requestElection(flags: DetailFlags) = with(flags) {
        viewModelScope.launch {
            if (state.value is Content) action.value = Refreshing

            when {
                liveLocalElectionIds != null -> useCase.getLocalElection(liveLocalElectionIds).handleFlow()
                liveRegionalElectionId != null -> useCase.getRegionalElection(liveRegionalElectionId).handleFlow()
                isLiveGeneralElection -> requestGeneralLiveCongressElection()
                election != null -> state.value = election.toContent()
                else -> handleError()
            }
        }
    }

    private suspend fun Flow<Result<LiveElection>>.handleFlow() {
        collect { result ->
            result
                .onSuccess { state.value = it.election.toContent() }
                .onFailure(::handleError)
        }
    }

    private suspend fun requestGeneralLiveCongressElection() {
        useCase.getCongressElection().collect { result ->
            result
                .onFailure(::handleError)
                .onSuccess { state.value = it.election.toContent() }
        }
    }

    private fun handleError(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }

        if (state.value is Content)
            action.value = ShowErrorSnackbar(throwable?.message)
        else
            state.value = Error(throwable?.message)

    }

    private fun refresh(interaction: Refresh) = with(interaction) {
        currentElection
            ?.takeIf { it.chamberName == KEY_SENATE && flags.isLiveGeneralElection }
            ?.let { requestGeneralLiveSenateElection() }
            ?: requestElection(flags)
    }

    private fun requestGeneralLiveSenateElection() {
        viewModelScope.launch {
            if (state.value is Content) action.value = Refreshing

            useCase.getSenateElection().collect { senateResult ->
                senateResult
                    .onFailure(::handleError)
                    .onSuccess { state.value = it.election.toContent() }
            }
        }
    }

    private fun swap(interaction: Swap): Unit = with(interaction) {
        when (currentElection?.chamberName) {
            KEY_SENATE -> requestElection(flags)
            KEY_CONGRESS -> if (flags.isLiveGeneralElection)
                requestGeneralLiveSenateElection()
            else
                requestElection(flags.copy(election = senateElection))
        }
    }
}
