package com.n27.regional_live.ui.regional_live.locals.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.regional_live.ui.regional_live.locals.comm.LocalsEvent
import com.n27.regional_live.ui.regional_live.locals.comm.LocalsEvent.ShowProvinces
import com.n27.regional_live.ui.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Failure
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Loading
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.ProvincesReceived
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

class MunicipalitySelectionViewModel @Inject constructor(
    private val repository: LiveRepository,
    eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableLiveData<MunicipalityState>(Loading)
    internal val viewState: LiveData<MunicipalityState> = state

    init {
        eventBus.event
            .onEach(::onEvent)
            .launchIn(viewModelScope)
    }

    private fun onEvent(event: LocalsEvent) {
        when (event) {
            is ShowProvinces -> state.value = ProvincesReceived(event.provinces)
        }
    }

    internal fun requestMunicipalities(province: String) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            state.value = Success(repository.getMunicipalities(province))
        }
    }
}
