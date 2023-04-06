package com.n27.regional_live.regionals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.regionals.models.RegionalsAction
import com.n27.regional_live.regionals.models.RegionalsAction.ShowErrorSnackbar
import com.n27.regional_live.regionals.models.RegionalsState
import com.n27.regional_live.regionals.models.RegionalsState.Content
import com.n27.regional_live.regionals.models.RegionalsState.Error
import com.n27.regional_live.regionals.models.RegionalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<RegionalsState>(Loading)
    internal val viewState = state.asStateFlow()
    private var lastState: RegionalsState = Loading

    private val action = Channel<RegionalsAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections() {
        lastState = state.value

        viewModelScope.launchCatching(::error) {
            state.emit(Loading)
            val elections = repository.getRegionalElections()

            if (elections.isNotEmpty())
                state.emit(Content(elections, repository.getParties()))
            else
                manageError()
        }
    }

    private suspend fun error(throwable: Throwable) { manageError(throwable.message) }

    private suspend fun manageError(error: String? = null) {
        if (lastState is Content) {
            action.send(ShowErrorSnackbar(error))
            state.emit(lastState)
        } else {
            state.emit(Error(error))
        }
    }
}
