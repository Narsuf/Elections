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
import com.n27.regional_live.locals.dialog.MunicipalityState.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class MunicipalitySelectionViewModel @Inject constructor(
    private val repository: LiveRepository,
    private val eventBus: LocalsEventBus
) : ViewModel() {

    private val state = MutableLiveData<MunicipalityState>(Loading)
    internal val viewState: LiveData<MunicipalityState> = state

    internal fun requestProvinces(region: Region?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            state.value = region?.let {
                val provinces = repository.getProvinces(region.name)
                Provinces(provinces)
            } ?: Failure()
        }
    }

    internal fun requestMunicipalities(province: Province?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            state.value = province?.let {
                val provinces = repository.getMunicipalities(province.name)
                Municipalities(provinces)
            } ?: Failure()
        }
    }

    internal fun requestElection(regionId: String?, provinceId: String?, municipalityId: String?) {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            val event = if (regionId != null && provinceId != null && municipalityId != null)
                RequestElection(LocalElectionIds(regionId, provinceId, municipalityId))
            else
                ShowError()

            eventBus.emit(event)
        }
    }
}
