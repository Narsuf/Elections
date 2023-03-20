package com.n27.regional_live.ui.regional_live.regionals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.regional_live.data.RegionalLiveRepository
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(
    private val repository: RegionalLiveRepository
) : ViewModel() {

    private val state = MutableLiveData<RegionalsState>(Loading)
    internal val viewState: LiveData<RegionalsState> = state

    fun requestElections() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            val elections = repository.getRegionalElections(2019)
            state.value = if (elections.isNotEmpty()) Success(elections) else Failure()
        }
    }
}
