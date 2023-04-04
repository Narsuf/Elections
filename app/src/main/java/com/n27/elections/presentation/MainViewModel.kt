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
import com.n27.elections.presentation.models.MainState.Error
import com.n27.elections.presentation.models.MainState.InitialLoading
import com.n27.elections.presentation.models.MainState.Loading
import com.n27.elections.presentation.models.MainState.Content
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

    private val state = MutableStateFlow<MainState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    private val action = Channel<MainAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections(initialLoading: Boolean = false) {
        viewModelScope.launchCatching(::error) {
            if (!initialLoading) state.emit(Loading)
            if (appRepository.isFirstLaunch()) action.send(ShowDisclaimer)

            val sortedElections = electionRepository.getElections()
                .map { it.sortResultsByElectsAndVotes() }
                .sortByDateAndFormat()

            state.emit(Content(sortedElections))
        }
    }

    internal fun saveFirstLaunchFlag() {
        viewModelScope.launchCatching(::error) { appRepository.saveFirstLaunchFlag() }
    }

    private suspend fun error(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)
        manageError(throwable.message)
    }

    private suspend fun manageError(error: String? = null) {
        if (state.value is Content)
            action.send(ShowErrorSnackbar(error))
        else
            state.emit(Error(error))
    }
}
