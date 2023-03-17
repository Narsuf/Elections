package com.n27.regional_live.ui.regional_live.regionals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n27.regional_live.data.RegionalLiveRepository
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

class RegionalsViewModel @Inject constructor(private val repository: RegionalLiveRepository): ViewModel() {

    private val _text = MutableLiveData<String>().apply { value = "This is home Fragment" }
    val text: LiveData<String> = _text

    fun apiRequest() {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println(throwable.message)
        }

        viewModelScope.launch(exceptionHandler) {
            val election = repository.getElection()
            println(election)
        }
    }
}
