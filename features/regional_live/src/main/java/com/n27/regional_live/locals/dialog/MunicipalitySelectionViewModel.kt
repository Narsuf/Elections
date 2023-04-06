package com.n27.regional_live.locals.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.local.json.models.Province
import com.n27.core.data.local.json.models.Region
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.launchCatching
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.locals.dialog.models.MunicipalityAction
import com.n27.regional_live.locals.dialog.models.MunicipalityAction.PopulateMunicipalitiesSpinner
import com.n27.regional_live.locals.dialog.models.MunicipalityAction.ShowErrorSnackbar
import com.n27.regional_live.locals.dialog.models.MunicipalityState
import com.n27.regional_live.locals.dialog.models.MunicipalityState.Content
import com.n27.regional_live.locals.dialog.models.MunicipalityState.Empty
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MunicipalitySelectionViewModel @Inject constructor(
    private val repository: LiveRepository,
    private val eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableStateFlow<MunicipalityState>(Empty)
    internal val viewState = state.asStateFlow()

    private val action = Channel<MunicipalityAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestProvinces(region: Region?) {
        viewModelScope.launchCatching(::error) {
            region?.let {
                val provinces = repository.getProvinces(region.name)
                state.emit(Content(provinces))
            } ?: action.send(ShowErrorSnackbar())
        }
    }

    internal fun requestMunicipalities(province: Province?) {
        viewModelScope.launchCatching(::error) {
            val resultAction = province?.let {
                val provinces = repository.getMunicipalities(province.name)
                PopulateMunicipalitiesSpinner(provinces)
            } ?: ShowErrorSnackbar()

            action.send(resultAction)
        }
    }

    internal fun requestElection(regionId: String?, provinceId: String?, municipalityId: String?) {
        viewModelScope.launchCatching(::error) {
            val event = if (regionId != null && provinceId != null && municipalityId != null)
                RequestElection(LocalElectionIds(regionId, provinceId, municipalityId))
            else
                ShowError()

            eventBus.emit(event)
        }
    }

    private suspend fun error(throwable: Throwable) {
        action.send(ShowErrorSnackbar(throwable.message))
    }
}
