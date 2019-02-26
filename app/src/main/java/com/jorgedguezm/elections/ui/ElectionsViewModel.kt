package com.jorgedguezm.elections.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.source.ElectionRepository
import com.jorgedguezm.elections.data.source.PartyRepository

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

import java.util.concurrent.TimeUnit

import javax.inject.Inject

class ElectionsViewModel @Inject constructor(
        private val electionRepository: ElectionRepository,
        private val partiesRepository: PartyRepository) : ViewModel() {

    var electionsResult: MutableLiveData<List<Election>> = MutableLiveData()
    var electionsError: MutableLiveData<String> = MutableLiveData()
    lateinit var electionsDisposableObserver: DisposableObserver<List<Election>>

    var partiesResult: MutableLiveData<List<Party>> = MutableLiveData()
    var partiesError: MutableLiveData<String> = MutableLiveData()
    lateinit var partiesDisposableObserver: DisposableObserver<List<Party>>

    fun electionsResult(): LiveData<List<Election>> { return electionsResult }

    fun electionsError(): LiveData<String> { return electionsError }

    fun partiesResult():LiveData<List<Party>> { return partiesResult }

    fun partiesError(): LiveData<String> { return partiesError }

    fun loadElections() {

        electionsDisposableObserver = object : DisposableObserver<List<Election>>() {
            override fun onComplete() { }

            override fun onNext(elections: List<Election>) {
                electionsResult.postValue(elections)
            }

            override fun onError(e: Throwable) {
                electionsError.postValue(e.message)
            }
        }

        electionRepository.getElections()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(electionsDisposableObserver)
    }

    fun loadParties() {

        partiesDisposableObserver = object : DisposableObserver<List<Party>>() {
            override fun onComplete() { }

            override fun onNext(parties: List<Party>) {
                partiesResult.postValue(parties)
            }

            override fun onError(e: Throwable) {
                partiesError.postValue(e.message)
            }
        }

        partiesRepository.getParties()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(partiesDisposableObserver)
    }

    fun disposeElements() {
        if (::electionsDisposableObserver.isInitialized && !electionsDisposableObserver.isDisposed)
            electionsDisposableObserver.dispose()

        if (::partiesDisposableObserver.isInitialized && !partiesDisposableObserver.isDisposed)
            partiesDisposableObserver.dispose()
    }
}