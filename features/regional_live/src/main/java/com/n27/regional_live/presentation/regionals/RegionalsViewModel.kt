package com.n27.regional_live.presentation.regionals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.domain.LiveUseCase
import com.n27.regional_live.presentation.regionals.entities.RegionalsAction
import com.n27.regional_live.presentation.regionals.entities.RegionalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.regionals.entities.RegionalsState
import com.n27.regional_live.presentation.regionals.entities.RegionalsState.Content
import com.n27.regional_live.presentation.regionals.entities.RegionalsState.Error
import com.n27.regional_live.presentation.regionals.entities.RegionalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(
    private val useCase: LiveUseCase,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

    private val state = MutableLiveData<RegionalsState>(Loading)
    internal val viewState: LiveData<RegionalsState> = state

    private val action = Channel<RegionalsAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        viewModelScope.launch {
            useCase.getRegionalElections()
                .onSuccess { state.value = Content(it) }
                .onFailure {
                    crashlytics?.recordException(it)

                    if (state.value is Content)
                        action.send(ShowErrorSnackbar(it.message))
                    else
                        state.value = Error(it.message)
                }
        }
    }
}
