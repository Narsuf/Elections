package com.n27.regional_live.ui.regional_live.locals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.core.data.LiveRepository
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Failure
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Loading
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Success
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocalsViewModel @Inject constructor(private val repository: LiveRepository) : ViewModel() {

    private val state = MutableLiveData<LocalsState>(Loading)
    internal val viewState: LiveData<LocalsState> = state

    fun requestRegions(initialLoading: Boolean = false) {
        if (initialLoading) state.value = Loading

        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            state.value = Failure(throwable)
        }

        viewModelScope.launch(exceptionHandler) {
            val regions = repository.getRegions()
            state.value = regions?.let { Success(it.regions) } ?: Failure()
        }
    }
}
