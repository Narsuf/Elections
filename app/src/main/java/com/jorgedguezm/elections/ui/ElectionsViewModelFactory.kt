package com.jorgedguezm.elections.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject

class ElectionsViewModelFactory @Inject constructor(
        private val electionsViewModel: ElectionsViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ElectionsViewModel::class.java!!))
            return electionsViewModel as T

        throw IllegalArgumentException("Unknown class name")
    }
}