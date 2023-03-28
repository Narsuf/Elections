package com.n27.elections.presentation

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.core.Constants.NOT_FIRST_LAUNCH
import com.n27.core.data.models.Election
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.core.presentation.PresentationUtils
import com.n27.elections.data.ElectionRepository
import com.n27.elections.presentation.entities.MainEvent
import com.n27.elections.presentation.entities.MainEvent.NavigateToDetail
import com.n27.elections.presentation.entities.MainEvent.NavigateToLive
import com.n27.elections.presentation.entities.MainInteraction
import com.n27.elections.presentation.entities.MainInteraction.DialogDismissed
import com.n27.elections.presentation.entities.MainInteraction.LiveButtonClicked
import com.n27.elections.presentation.entities.MainInteraction.Refresh
import com.n27.elections.presentation.entities.MainInteraction.ScreenOpened
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Idle
import com.n27.elections.presentation.entities.MainState.Loading
import com.n27.elections.presentation.entities.MainState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val repository: ElectionRepository,
    private val utils: PresentationUtils,
    internal var sharedPreferences: SharedPreferences
) : ViewModel() {

    private val state = MutableLiveData<MainState>(Idle)
    internal val viewState: LiveData<MainState> = state

    private val event = Channel<MainEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    internal fun handleInteraction(action: MainInteraction) = when (action) {
        ScreenOpened -> retrieveElections(initialLoading = true)
        DialogDismissed -> saveFirstLaunchFlag()
        Refresh -> retrieveElections()
        LiveButtonClicked -> onLiveButtonClicked()
    }

    private fun retrieveElections(initialLoading: Boolean = false) {
        if (initialLoading) state.value = Loading
        if (!sharedPreferences.contains(NOT_FIRST_LAUNCH)) event.trySend(MainEvent.ShowDisclaimer)

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            Firebase.crashlytics.recordException(throwable)
            state.value = Error(throwable.message)
        }

        viewModelScope.launch(exceptionHandler) {
            val sortedElections = repository.getElections()
                .map { it.sortResultsByElectsAndVotes() }
                .sortByDateAndFormat()

            state.value = Success(sortedElections, ::onElectionClicked)

            if (initialLoading) utils.track("main_activity_loaded") { param("state", "success") }
        }
    }

    private fun saveFirstLaunchFlag() {
        utils.track("dialog_dismissed")
        sharedPreferences.edit().putBoolean(NOT_FIRST_LAUNCH, true).apply()
    }

    @VisibleForTesting
    internal fun onElectionClicked(congressElection: Election, senateElection: Election) {
        utils.track("election_clicked") { param("election", congressElection.date) }
        event.trySend(NavigateToDetail(congressElection, senateElection))
    }

    @VisibleForTesting
    internal fun onLiveButtonClicked() {
        utils.track("live_button_clicked")
        event.trySend(NavigateToLive)
    }
}
