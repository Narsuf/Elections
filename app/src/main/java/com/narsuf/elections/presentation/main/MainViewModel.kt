package com.narsuf.elections.presentation.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.narsuf.elections.data.ElectionRepository
import com.narsuf.elections.data.models.Election
import com.narsuf.elections.presentation.common.extensions.sortByDateAndFormat
import com.narsuf.elections.presentation.common.extensions.sortResultsByElectsAndVotes
import com.narsuf.elections.presentation.common.extensions.track
import com.narsuf.elections.presentation.main.entities.MainEvent
import com.narsuf.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.narsuf.elections.presentation.main.entities.MainInteraction
import com.narsuf.elections.presentation.main.entities.MainInteraction.Refresh
import com.narsuf.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.narsuf.elections.presentation.main.entities.MainState
import com.narsuf.elections.presentation.main.entities.MainState.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val electionRepository: ElectionRepository,
    private val analytics: FirebaseAnalytics,
    private val crashlytics: FirebaseCrashlytics,
) : ViewModel() {

    private val state = MutableLiveData<MainState>(Idle)
    internal val viewState: LiveData<MainState> = state

    private val event = Channel<MainEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    internal fun handleInteraction(action: MainInteraction) = when (action) {
        ScreenOpened -> retrieveElections(initialLoading = true)
        Refresh -> retrieveElections()
    }

    private fun retrieveElections(initialLoading: Boolean = false) {
        if (initialLoading) state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            crashlytics.recordException(throwable)
            state.value = Error()
        }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            electionRepository.getElections().collect { elections ->
                elections.onSuccess { election ->
                    val sortedElections = election
                        .map{ it.sortResultsByElectsAndVotes() }
                        .sortByDateAndFormat()
                    if (initialLoading) analytics.track("main_activity_loaded", "state", "success")
                    state.value = Success(sortedElections, ::onElectionClicked)
                }.onFailure {
                    state.value = Error(it.message)
                }
            }
        }
    }

    @VisibleForTesting
    internal fun onElectionClicked(congressElection: Election, senateElection: Election) {
        analytics.track("election_clicked", "election", congressElection.date)
        event.trySend(NavigateToDetail(congressElection, senateElection))
    }
}
