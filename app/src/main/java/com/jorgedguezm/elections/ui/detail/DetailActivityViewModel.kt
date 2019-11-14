package com.jorgedguezm.elections.ui.detail

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

class DetailActivityViewModel @Inject constructor(
        private val electionRepository: ElectionRepository) : ViewModel() {

    var electionResult: MutableLiveData<Election> = MutableLiveData()
    var electionError: MutableLiveData<String> = MutableLiveData()
    lateinit var electionDisposableObserver: DisposableObserver<Election>

    fun electionResult(): LiveData<Election> { return electionResult }
    fun electionError(): LiveData<String> { return electionError }

    /*fun loadSenateElection(year: Int, place: String) {
        electionDisposableObserver = object : DisposableObserver<Election>() {
            override fun onComplete() { }

            override fun onNext(election: Election) { electionResult.postValue(election) }

            override fun onError(e: Throwable) { electionError.postValue(e.message) }
        }

        electionRepository.getElection(year, place, "Senado")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(electionDisposableObserver)
    }*/

    fun disposeElements() {
        if (::electionDisposableObserver.isInitialized && !electionDisposableObserver.isDisposed)
            electionDisposableObserver.dispose()
    }
}