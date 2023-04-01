package com.n27.elections.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.core.extensions.sortByDateAndFormat
import com.n27.core.extensions.sortResultsByElectsAndVotes
import com.n27.elections.data.repositories.AppRepository
import com.n27.elections.data.repositories.ElectionRepository
import com.n27.elections.presentation.entities.MainEvent
import com.n27.elections.presentation.entities.MainEvent.ShowDisclaimer
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.InitialLoading
import com.n27.elections.presentation.entities.MainState.Loading
import com.n27.elections.presentation.entities.MainState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val electionRepository: ElectionRepository
) : ViewModel() {

    private val state = MutableStateFlow<MainState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    private val event = Channel<MainEvent>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewEvent = event.receiveAsFlow()

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Firebase.crashlytics.recordException(throwable)
        state.tryEmit(Error(throwable.message))
    }

    internal fun requestElections(initialLoading: Boolean = false) {
        viewModelScope.launch(exceptionHandler) {
            if (!initialLoading) state.emit(Loading)
            if (appRepository.isFirstLaunch()) event.send(ShowDisclaimer)
            val sortedElections = electionRepository.getElections()

            state.emit(Success(sortedElections))
        }
    }

    internal fun saveFirstLaunchFlag() {
        viewModelScope.launch(exceptionHandler) { appRepository.saveFirstLaunchFlag() }
    }
}
