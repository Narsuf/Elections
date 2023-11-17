package com.n27.regional_live.presentation.locals.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.models.Province
import com.n27.core.domain.region.models.Region
import com.n27.core.presentation.PresentationUtils
import com.n27.regional_live.Constants.NULL_ID
import com.n27.regional_live.Constants.PROVINCE_EMPTY
import com.n27.regional_live.Constants.REGION_EMPTY
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityAction
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityAction.PopulateMunicipalitiesSpinner
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityState
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityState.Content
import com.n27.regional_live.presentation.locals.dialog.entities.MunicipalityState.Empty
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
            } ?: handleError(Throwable(REGION_EMPTY))
        }
    }

    internal fun requestMunicipalities(province: Province?) {
        viewModelScope.launch {
            province?.let {
                val provinces = useCase.getMunicipalities(province.name)

                utils?.track("municipality_selection_municipalities_loaded") {
                    param("province", "$province")
                }

                action.send(PopulateMunicipalitiesSpinner(provinces))
            } ?: handleError(Throwable(PROVINCE_EMPTY))
        }
    }

    internal fun requestElection(regionId: String?, provinceId: String?, municipalityId: String?) {
        viewModelScope.launch {
            val event = if (regionId != null && provinceId != null && municipalityId != null)
                RequestElection(LocalElectionIds(regionId, provinceId, municipalityId))
            else
                ShowError(Throwable(NULL_ID))

            eventBus.emit(event)
        }
    }

    private suspend fun handleError(throwable: Throwable) {
        crashlytics?.recordException(throwable)
        action.send(ShowErrorSnackbar)
    }
}
