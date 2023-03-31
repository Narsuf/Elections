package com.n27.regional_live.regionals

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.regionals.RegionalsState.InitialLoading
import com.n27.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.regionals.RegionalsState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableLiveData<RegionalsState>(InitialLoading)
    internal val viewState = state

    internal fun requestElections(initialLoading: Boolean = false) {
        if (!initialLoading) state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            val elections = repository.getRegionalElections()

            state.value = if (elections.isNotEmpty()) {
                Success(elections, repository.getParties())
            } else {
                Failure()
            }
        }
    }
}
