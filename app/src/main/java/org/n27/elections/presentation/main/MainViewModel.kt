package org.n27.elections.presentation.main

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import org.n27.elections.data.ElectionRepository
import org.n27.elections.data.models.Election
import org.n27.elections.presentation.common.Constants.NOT_FIRST_LAUNCH
import org.n27.elections.presentation.common.extensions.sortByDateAndFormat
import org.n27.elections.presentation.common.extensions.sortResultsByElectsAndVotes
import org.n27.elections.presentation.common.extensions.track
import org.n27.elections.presentation.main.entities.MainEvent
import org.n27.elections.presentation.main.entities.MainEvent.NavigateToDetail
import org.n27.elections.presentation.main.entities.MainInteraction
import org.n27.elections.presentation.main.entities.MainInteraction.DialogDismissed
import org.n27.elections.presentation.main.entities.MainInteraction.Refresh
import org.n27.elections.presentation.main.entities.MainInteraction.ScreenOpened
import org.n27.elections.presentation.main.entities.MainState
import org.n27.elections.presentation.main.entities.MainState.Error
import org.n27.elections.presentation.main.entities.MainState.Idle
import org.n27.elections.presentation.main.entities.MainState.Loading
import org.n27.elections.presentation.main.entities.MainState.Success
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
    }

    private fun retrieveElections(initialLoading: Boolean = false) {
        if (initialLoading) state.value = Loading
        if (!sharedPreferences.contains(NOT_FIRST_LAUNCH)) event.trySend(MainEvent.ShowDisclaimer)

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            crashlytics.recordException(throwable)
            state.value = Error(throwable.message)
        }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            if (initialLoading) analytics.track("main_activity_loaded") { param("state", "success") }
            val sortedElections = electionRepository.getElections()
                .map { it.sortResultsByElectsAndVotes() }
                .sortByDateAndFormat()
            state.value = Success(sortedElections, ::onElectionClicked)
        }
    }

    private fun saveFirstLaunchFlag() {
        analytics.track("dialog_dismissed")
        sharedPreferences.edit().putBoolean(NOT_FIRST_LAUNCH, true).apply()
    }

    @VisibleForTesting
    internal fun onElectionClicked(congressElection: Election, senateElection: Election) {
        analytics.track("election_clicked") { param("election", congressElection.date) }
        event.trySend(NavigateToDetail(congressElection, senateElection))
    }
}
