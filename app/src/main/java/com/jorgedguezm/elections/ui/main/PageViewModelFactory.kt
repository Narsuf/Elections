package com.jorgedguezm.elections.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import javax.inject.Inject

class PageViewModelFactory @Inject constructor(
        private val pageViewModel: PageViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageViewModel::class.java))
            return pageViewModel as T

        throw IllegalArgumentException("Unknown class name")
    }
}