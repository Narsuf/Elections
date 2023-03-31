package com.n27.regional_live.locals.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.core.data.api.models.LocalElectionIds
import com.n27.core.data.json.models.Province
import com.n27.core.data.json.models.Region
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.locals.dialog.MunicipalityState.Failure
import com.n27.regional_live.locals.dialog.MunicipalityState.Loading
import com.n27.regional_live.locals.dialog.MunicipalityState.Municipalities
import com.n27.regional_live.locals.dialog.MunicipalityState.Provinces
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MunicipalitySelectionViewModel @Inject constructor(
    private val repository: LiveRepository,
    private val eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableLiveData<MunicipalityState>(Loading)
    internal val viewState: LiveData<MunicipalityState> = state

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        state.value = Failure(throwable)
    }

    internal fun requestProvinces(region: Region?) {
        viewModelScope.launch(exceptionHandler) {
            state.value = region?.let {
                val provinces = repository.getProvinces(region.name)
                Provinces(provinces)
            } ?: Failure()
        }
    }

    internal fun requestMunicipalities(province: Province?) {
        viewModelScope.launch(exceptionHandler) {
            state.value = province?.let {
                val provinces = repository.getMunicipalities(province.name)
                Municipalities(provinces)
            } ?: Failure()
        }
    }

    internal fun requestElection(regionId: String?, provinceId: String?, municipalityId: String?) {
        viewModelScope.launch(exceptionHandler) {
            val event = if (regionId != null && provinceId != null && municipalityId != null)
                RequestElection(LocalElectionIds(regionId, provinceId, municipalityId))
            else
                ShowError()

            eventBus.emit(event)
        }
    }
}
