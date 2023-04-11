package com.n27.elections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.core.extensions.launchCatching
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.data.repositories.AppRepository
import com.n27.elections.data.repositories.ElectionRepository
import com.n27.elections.presentation.models.MainAction
import com.n27.elections.presentation.models.MainAction.ShowDisclaimer
import com.n27.elections.presentation.models.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.models.MainState
import com.n27.elections.presentation.models.MainState.Content
import com.n27.elections.presentation.models.MainState.Error
import com.n27.elections.presentation.models.MainState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val electionRepository: ElectionRepository
) : ViewModel() {

    private val state = MutableStateFlow<MainState>(Loading)
    internal val viewState = state.asStateFlow()
    private var lastState: MainState = Loading

    private val action = Channel<MainAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        lastState = state.value

        viewModelScope.launchCatching(::handleError) {
            state.emit(Loading)
            if (appRepository.isFirstLaunch()) action.send(ShowDisclaimer)

            val sortedElections = electionRepository.getElections()
                .map { it.sortResultsByElectsAndVotes() }
                .sortByDateAndFormat()
            state.emit(Content(sortedElections))
        }
    }

    internal fun saveFirstLaunchFlag() {
        viewModelScope.launchCatching(::handleError) { appRepository.saveFirstLaunchFlag() }
    }

    private suspend fun handleError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)

        if (lastState is Content)
            action.send(ShowErrorSnackbar(throwable.message))
        else
            state.emit(Error(throwable.message))
    }

}
