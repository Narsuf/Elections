package com.n27.elections.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.core.extensions.launchCatching
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.data.repositories.AppRepository
import com.n27.elections.data.repositories.ElectionRepositoryImpl
import com.n27.elections.presentation.models.MainAction
import com.n27.elections.presentation.models.MainAction.ShowDisclaimer
import com.n27.elections.presentation.models.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.models.MainState
import com.n27.elections.presentation.models.MainState.Content
import com.n27.elections.presentation.models.MainState.Error
import com.n27.elections.presentation.models.MainState.Loading
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val electionRepository: ElectionRepositoryImpl
) : ViewModel() {

    private val state = MutableLiveData<MainState>(Loading)
    internal val viewState: LiveData<MainState> = state

    private val action = Channel<MainAction>(capacity = 1, DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        viewModelScope.launchCatching(::handleError) {
            if (appRepository.isFirstLaunch()) action.send(ShowDisclaimer)

            electionRepository.getElections()
                .onFailure { handleError(it) }
                .onSuccess { elections ->
                    val sortedElections = elections.items
                        .map { it.sortResultsByElectsAndVotes() }
                        .sortByDateAndFormat()

                    state.value = Content(
                        congressElections = sortedElections.filter { it.chamberName == "Congreso" },
                        senateElections = sortedElections.filter { it.chamberName == "Senado" }
                    )
                }
        }
    }

    internal fun saveFirstLaunchFlag() {
        viewModelScope.launch { appRepository.saveFirstLaunchFlag() }
    }

    private suspend fun handleError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)

        if (state.value is Content)
            action.send(ShowErrorSnackbar(throwable.message))
        else
            state.value = Error(throwable.message)
    }
}
