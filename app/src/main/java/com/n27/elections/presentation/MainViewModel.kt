package com.n27.elections.presentation

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
import com.n27.elections.presentation.models.MainContentState
import com.n27.elections.presentation.models.MainContentState.Empty
import com.n27.elections.presentation.models.MainContentState.WithData
import com.n27.elections.presentation.models.MainState
import com.n27.elections.presentation.models.MainState.Content
import com.n27.elections.presentation.models.MainState.Error
import com.n27.elections.presentation.models.MainState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val electionRepository: ElectionRepositoryImpl
) : ViewModel() {

    private val contentState = MutableStateFlow<MainContentState>(Empty)
    internal val viewContentState = contentState.asStateFlow()

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

            electionRepository.getElections()
                .onFailure { handleError(it) }
                .onSuccess { elections ->
                    val sortedElections = elections.items
                        .map { it.sortResultsByElectsAndVotes() }
                        .sortByDateAndFormat()

                    contentState.emit(WithData(sortedElections))
                    state.emit(Content)
                }
        }
    }

    internal fun saveFirstLaunchFlag() {
        viewModelScope.launch { appRepository.saveFirstLaunchFlag() }
    }

    private suspend fun handleError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)

        if (lastState is Content) {
            action.send(ShowErrorSnackbar(throwable.message))
            state.emit(Content)
        } else {
            state.emit(Error(throwable.message))
        }
    }

}
