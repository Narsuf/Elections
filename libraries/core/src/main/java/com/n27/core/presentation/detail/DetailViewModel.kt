package com.n27.core.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LiveElection
import com.n27.core.presentation.detail.entities.DetailAction
import com.n27.core.presentation.detail.entities.DetailAction.Refreshing
import com.n27.core.presentation.detail.entities.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.entities.DetailFlags
import com.n27.core.presentation.detail.entities.DetailInteraction
import com.n27.core.presentation.detail.entities.DetailInteraction.Refresh
import com.n27.core.presentation.detail.entities.DetailInteraction.ScreenOpened
import com.n27.core.presentation.detail.entities.DetailInteraction.Swap
import com.n27.core.presentation.detail.entities.DetailState
import com.n27.core.presentation.detail.entities.DetailState.Content
import com.n27.core.presentation.detail.entities.DetailState.Error
import com.n27.core.presentation.detail.entities.DetailState.Loading
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
            when {
                liveLocalElectionIds != null -> {
                    loading()
                    useCase.getLocalElection(liveLocalElectionIds).handleResult()
                }

                liveRegionalElectionId != null -> {
                    loading()
                    useCase.getRegionalElection(liveRegionalElectionId).handleResult()
                }

                isLiveGeneralElection -> {
                    loading()
                    requestGeneralLiveCongressElection()
                }

                election != null -> state.value = Content(election)
                else -> handleError()
            }
        }
    }

    private fun loading() {
        if (state.value is Content) action.value = Refreshing
    }

    private fun Result<LiveElection>.handleResult() = onSuccess {
        state.value = Content(it.election)
    }.onFailure(::handleError)

    private suspend fun requestGeneralLiveCongressElection() {
        useCase.getCongressElection()
            .onFailure(::handleError)
            .onSuccess { state.value = Content(it.election) }
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
            loading()

            useCase.getSenateElection()
                .onFailure(::handleError)
                .onSuccess { state.value = Content(it.election) }
        }
    }

    private fun swap(interaction: Swap) = with(interaction) {
        when (currentElection?.chamberName) {
            KEY_SENATE -> requestElection(flags)
            KEY_CONGRESS -> if (flags.isLiveGeneralElection)
                requestGeneralLiveSenateElection()
            else
                requestElection(flags.copy(election = senateElection))

            else -> handleError()
        }
    }
}
