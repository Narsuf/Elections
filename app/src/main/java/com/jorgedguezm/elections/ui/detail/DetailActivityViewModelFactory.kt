package com.jorgedguezm.elections.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject

class DetailActivityViewModelFactory @Inject constructor(
        private val detailActivityViewModel: DetailActivityViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailActivityViewModel::class.java))
            return detailActivityViewModel as T

        throw IllegalArgumentException("Unknown class name")
    }
}