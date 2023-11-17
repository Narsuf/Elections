package com.n27.regional_live.presentation.locals.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.data.remote.api.LiveRepositoryImpl
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Region
import com.n27.core.presentation.PresentationUtils
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityAction
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityAction.PopulateMunicipalitiesSpinner
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityState
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityState.Content
import com.n27.regional_live.presentation.locals.dialog.models.MunicipalityState.Empty
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MunicipalitySelectionViewModel @Inject constructor(
    private val useCase: LiveUseCase,
    private val eventBus: LocalsEventBus,
    private val utils: PresentationUtils?,
    private val crashlytics: FirebaseCrashlytics?
) : ViewModel() {

    private val state = MutableStateFlow<MunicipalityState>(Empty)
    internal val viewState = state.asStateFlow()

    private val action = Channel<MunicipalityAction>(capacity = 1, BufferOverflow.DROP_OLDEST)
    internal val viewAction = action.receiveAsFlow()

    internal fun requestProvinces(region: Region?) {
        viewModelScope.launch {
            region?.let {
                val provinces = useCase.getProvinces(region.name)
                state.emit(Content(provinces))

                utils?.track("municipality_selection_provinces_loaded") { param("region", "$region") }
            } ?: handleError()
        }
    }

    internal fun requestMunicipalities(province: Province?) {
        viewModelScope.launch {
            val resultAction = province?.let {
                val provinces = useCase.getMunicipalities(province.name)
                    .sortedBy { it.name }

                utils?.track("municipality_selection_municipalities_loaded") {
                    param("province", "$province")
                }

                PopulateMunicipalitiesSpinner(provinces)
            } ?: ShowErrorSnackbar()

            action.send(resultAction)
        }
    }

    internal fun requestElection(regionId: String?, provinceId: String?, municipalityId: String?) {
        viewModelScope.launch {
            val event = if (regionId != null && provinceId != null && municipalityId != null)
                RequestElection(LocalElectionIds(regionId, provinceId, municipalityId))
            else
                ShowError

            eventBus.emit(event)
        }
    }

    private suspend fun handleError(throwable: Throwable? = null) {
        throwable?.let { crashlytics?.recordException(it) }
        action.send(ShowErrorSnackbar(throwable?.message))
    }
}
