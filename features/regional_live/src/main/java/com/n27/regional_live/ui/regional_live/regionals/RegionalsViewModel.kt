package com.n27.regional_live.ui.regional_live.regionals

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(
    private val repository: LiveRepository
) : ViewModel() {

    private val state = MutableLiveData<RegionalsState>(Loading)
    internal val viewState: LiveData<RegionalsState> = state

    fun requestElections(initialLoading: Boolean = false) {
        if (initialLoading) state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
            Log.e("error", throwable.toString())
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
