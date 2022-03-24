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

    fun loadElections(place: String? = null, chamber: String? = null) {
        _electionsResult.value = MainViewState.Loading

        electionsExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            _electionsResult.value = MainViewState.Error(throwable)
        }

        electionsJob = viewModelScope.launch(Dispatchers.Main + electionsExceptionHandler) {
            val elections = electionRepository.loadElections(place, chamber)
            val sortedElections = sortElections(elections)
            _electionsResult.value = MainViewState.Success(sortedElections)
        }
    }

    internal fun sortElections(elections: List<Election>): List<Election> {
        val electionsCopy = elections.map { it.copy() }

        electionsCopy.sortedByDescending {
            if (it.date.length > 4)
                it.date.toDouble() / 10
            else
                it.date.toDouble()
        }.let { sortedElections ->
            sortedElections.forEach {
                if (it.date.length > 4) {
                    it.date = when (it.date) {
                        "20192" -> "2019-10N"
                        "20191" -> "2019-28A"
                        else -> it.date
                    }
                }
            }

            return sortedElections
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
