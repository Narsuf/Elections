package com.jorgedguezm.elections.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.repository.ElectionRepository

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

import javax.inject.Inject

class PlaceholderViewModel @Inject constructor(private val electionRepository: ElectionRepository) :
    ViewModel() {

    private val _electionsResult: MutableLiveData<MainViewState> = MutableLiveData()

    val electionsResult: LiveData<MainViewState> = _electionsResult

    var electionsJob: Job? = null
    lateinit var electionsExceptionHandler: CoroutineExceptionHandler

    fun loadElections(place: String, chamber: String? = null) {
        _electionsResult.value = MainViewState.Loading

        electionsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _electionsResult.value = MainViewState.Error(throwable)
        }

        electionsJob = viewModelScope.launch(Dispatchers.Main + electionsExceptionHandler) {
            val elections = electionRepository.loadElections(place, chamber)
            _electionsResult.value = MainViewState.Success(elections)
        }
    }

    override fun onCleared() {
        electionsJob?.cancel()
        super.onCleared()
    }
}

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(val elections: List<Election>) : MainViewState()
    data class Error(val throwable: Throwable) : MainViewState()
}
