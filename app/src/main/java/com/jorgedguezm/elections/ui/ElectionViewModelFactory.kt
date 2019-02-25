package com.jorgedguezm.elections.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject

class ElectionViewModelFactory @Inject constructor(
        private val electionViewModel: ElectionViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionViewModel::class.java!!))
            return electionViewModel as T

        throw IllegalArgumentException("Unknown class name")
    }
}