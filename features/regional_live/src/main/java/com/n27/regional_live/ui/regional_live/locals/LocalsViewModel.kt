package com.n27.regional_live.ui.regional_live.locals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocalsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply { value = "This is dashboard Fragment" }
    val text: LiveData<String> = _text
}
