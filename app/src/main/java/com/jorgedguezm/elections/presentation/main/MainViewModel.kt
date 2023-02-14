package com.jorgedguezm.elections.presentation.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.jorgedguezm.elections.data.ElectionRepository
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.presentation.common.extensions.sortByDate
import com.jorgedguezm.elections.presentation.common.extensions.sortResultsByElectsAndVotes
import com.jorgedguezm.elections.presentation.common.extensions.track
import com.jorgedguezm.elections.presentation.main.entities.MainEvent
import com.jorgedguezm.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction.Refresh
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.jorgedguezm.elections.presentation.main.entities.MainState
import com.jorgedguezm.elections.presentation.main.entities.MainState.Error
import com.jorgedguezm.elections.presentation.main.entities.MainState.Idle
import com.jorgedguezm.elections.presentation.main.entities.MainState.Loading
import com.jorgedguezm.elections.presentation.main.entities.MainState.Success
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
    private val crashlytics: FirebaseCrashlytics
) : ViewModel() {

    private val state = MutableLiveData<MainState>(Idle)
    internal val viewState: LiveData<MainState> = state

    private val event = Channel<MainEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    internal fun handleInteraction(action: MainInteraction) = when (action) {
        ScreenOpened -> retrieveElections(loadAnimation = true)
        Refresh -> retrieveElections()
    }

    private fun retrieveElections(loadAnimation: Boolean = false, fallback: Boolean = false) {
        if (loadAnimation) state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            if (throwable.message != null && throwable.message!!.contains("Failed to connect to ")) {
                if (!fallback) {
                    crashlytics.recordException(Exception("Main service down"))
                    retrieveElections(fallback = true)
                } else {
                    crashlytics.recordException(Exception("Firebase down"))
                    state.value = Error(throwable.message)
                }
            } else {
                crashlytics.recordException(throwable)
                state.value = Error(throwable.message)
            }
        }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            electionRepository.getElections(fallback).collect { elections ->
                val sortedElections = elections.sortByDate().sortResultsByElectsAndVotes()
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
