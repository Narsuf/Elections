package com.jorgedguezm.elections.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.source.ElectionRepository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit

import javax.inject.Inject

class ElectionsViewModel @Inject constructor(
        private val electionRepository: ElectionRepository) : ViewModel() {

    var electionsResult: MutableLiveData<List<Election>> = MutableLiveData()
    var electionsError: MutableLiveData<String> = MutableLiveData()
    lateinit var electionsDisposableObserver: DisposableObserver<List<Election>>

    fun electionsResult(): LiveData<List<Election>> { return electionsResult }
    fun electionsError(): LiveData<String> { return electionsError }

    fun loadCongressElections() {
        electionsDisposableObserver = object : DisposableObserver<List<Election>>() {
            override fun onComplete() { }

            override fun onNext(elections: List<Election>) { electionsResult.postValue(elections) }

            override fun onError(e: Throwable) { electionsError.postValue(e.message) }
        }

        electionRepository.getElections("España", "Congreso")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(electionsDisposableObserver)
    }

    fun disposeElements() {
        if (::electionsDisposableObserver.isInitialized && !electionsDisposableObserver.isDisposed)
            electionsDisposableObserver.dispose()
    }
}