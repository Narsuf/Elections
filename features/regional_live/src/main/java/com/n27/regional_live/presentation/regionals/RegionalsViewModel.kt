package com.n27.regional_live.presentation.regionals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.presentation.regionals.models.RegionalsAction
import com.n27.regional_live.presentation.regionals.models.RegionalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.regionals.models.RegionalsContentState
import com.n27.regional_live.presentation.regionals.models.RegionalsContentState.Empty
import com.n27.regional_live.presentation.regionals.models.RegionalsContentState.WithData
import com.n27.regional_live.presentation.regionals.models.RegionalsState
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Content
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Error
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(
    private val repository: LiveRepositoryImpl,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

    private val contentState = MutableStateFlow<RegionalsContentState>(Empty)
    internal val viewContentState = contentState.asStateFlow()

    private val state = MutableStateFlow<RegionalsState>(Loading)
    internal val viewState = state.asStateFlow()
    private var lastState: RegionalsState = Loading

    private val action = Channel<RegionalsAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        lastState = state.value

        viewModelScope.launchCatching(::handleError) {
            state.emit(Loading)

            repository.getRegionalElections().collect { result ->
                result
                    .onFailure { handleError(it) }
                    .onSuccess {
                        contentState.emit(WithData(it))
                        state.emit(Content)
                    }
            }
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        crashlytics?.recordException(throwable)

        if (lastState is Content) {
            action.send(ShowErrorSnackbar(throwable.message))
            state.emit(Content)
        } else {
            state.emit(Error(throwable.message))
        }
    }
}
