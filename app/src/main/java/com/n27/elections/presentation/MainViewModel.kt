package com.n27.elections.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.n27.elections.data.AppRepositoryImpl
import com.n27.elections.domain.ElectionUseCase
import com.n27.elections.presentation.entities.MainAction
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.elections.presentation.entities.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.Content
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Loading
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val appRepository: AppRepositoryImpl,
    private val useCase: ElectionUseCase
) : ViewModel() {

    private val state = MutableLiveData<MainState>(Loading)
    internal val viewState: LiveData<MainState> = state

    private val action = Channel<MainAction>(capacity = 1, DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        viewModelScope.launch {
            //if (appRepository.isFirstLaunch()) action.send(ShowDisclaimer)

            useCase.getElections().collect { result ->
                result
                    .onFailure { handleError(it) }
                    .onSuccess {
                        state.value = Content(
                            congressElections = it.congress,
                            senateElections = it.senate
                        )
                    }
            }
        }
    }

    internal fun saveFirstLaunchFlag() { appRepository.saveFirstLaunch() }

    private suspend fun handleError(throwable: Throwable) {
        Firebase.crashlytics.recordException(throwable)

        if (state.value is Content)
            action.send(ShowErrorSnackbar(throwable.message))
        else
            state.value = Error(throwable.message)
    }
}
