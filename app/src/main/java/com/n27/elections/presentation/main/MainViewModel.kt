package com.n27.elections.presentation.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.elections.data.ElectionRepository
import com.n27.elections.data.models.Election
import com.n27.elections.presentation.common.extensions.sortByDateAndFormat
import com.n27.elections.presentation.common.extensions.sortResultsByElectsAndVotes
import com.n27.elections.presentation.common.extensions.track
import com.n27.elections.presentation.main.entities.MainEvent
import com.n27.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.n27.elections.presentation.main.entities.MainInteraction
import com.n27.elections.presentation.main.entities.MainInteraction.Refresh
import com.n27.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.n27.elections.presentation.main.entities.MainState
import com.n27.elections.presentation.main.entities.MainState.*
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
            state.value = Error(throwable.message)
        }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            electionRepository.getElections().collect { elections ->
                if (initialLoading) analytics.track("main_activity_loaded", "state", "success")
                val sortedElections = elections
                    .map { it.sortResultsByElectsAndVotes() }
                    .sortByDateAndFormat()
                state.value = Success(sortedElections, ::onElectionClicked)
            }
        }
    }

    @VisibleForTesting
    internal fun onElectionClicked(congressElection: Election, senateElection: Election) {
        analytics.track("election_clicked", "election", congressElection.date)
        event.trySend(NavigateToDetail(congressElection, senateElection))
    }
}
