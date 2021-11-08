package com.jorgedguezm.elections.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.repository.ElectionRepository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers

import javax.inject.Inject

class PlaceholderViewModel @Inject constructor(
        private val electionRepository: ElectionRepository) : ViewModel() {

    private val _index = MutableLiveData<Int>()

    fun setIndex(index: Int) { _index.value = index }

    private val electionsDisposableObserver = CompositeDisposable()
    private val _electionsResult: MutableLiveData<MainViewState> = MutableLiveData()

    val electionsResult: LiveData<MainViewState> = _electionsResult

    fun loadElections(place: String, chamber: String? = null) {
        _electionsResult.value = MainViewState.Loading

        electionsDisposableObserver += electionRepository.loadElections(place, chamber)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onNext = { _electionsResult.postValue(MainViewState.Success(it.elections)) },
                onError = { _electionsResult.postValue(MainViewState.Error(it)) }
            )
    }

    override fun onCleared() {
        super.onCleared()
        electionsDisposableObserver.dispose()
    }
}

sealed class MainViewState {
    object Loading : MainViewState()

    data class Success(val elections: List<Election>) : MainViewState()
    data class Error(val throwable: Throwable) : MainViewState()
}
