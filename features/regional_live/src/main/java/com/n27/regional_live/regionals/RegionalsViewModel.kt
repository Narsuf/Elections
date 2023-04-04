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
import com.n27.regional_live.regionals.models.RegionalsState.InitialLoading
import com.n27.regional_live.regionals.models.RegionalsState.Loading
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableStateFlow<RegionalsState>(InitialLoading)
    internal val viewState = state.asStateFlow()

    private val action = Channel<RegionalsAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestElections(initialLoading: Boolean = false) {
        viewModelScope.launchCatching(::error) {
            if (!initialLoading) state.emit(Loading)
            val elections = repository.getRegionalElections()

            if (elections.isNotEmpty())
                state.emit(Content(elections, repository.getParties()))
            else
                manageError()
        }
    }

    private suspend fun error(throwable: Throwable) { manageError(throwable.message) }

    private suspend fun manageError(error: String? = null) {
        if (state.value is Content)
            action.send(ShowErrorSnackbar(error))
        else
            state.emit(Error(error))
    }
}
