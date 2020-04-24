package com.jorgedguezm.elections.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.models.Resource
import com.jorgedguezm.elections.repository.ElectionRepository
import com.jorgedguezm.elections.utils.AbsentLiveData

import javax.inject.Inject

class PlaceholderViewModel @Inject constructor(
        private val electionRepository: ElectionRepository) : ViewModel() {

    private val _index = MutableLiveData<Int>()

    fun setIndex(index: Int) { _index.value = index }

    val electionsResult: LiveData<Resource<List<Election>>>

    private val election: MutableLiveData<Pair<String, String?>> = MutableLiveData()

    init {
        electionsResult = election.switchMap {
            election.value?.let {
                electionRepository.loadElections(it.first, it.second)
            } ?: AbsentLiveData.create()
        }
    }

    fun postElection(placeAndChamber: Pair<String, String?>) = election.postValue(placeAndChamber)
}