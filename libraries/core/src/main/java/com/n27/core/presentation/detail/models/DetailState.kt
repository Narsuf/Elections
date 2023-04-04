package com.n27.core.presentation.detail.models

import com.n27.core.data.models.Election

sealed class DetailState {

    object Loading : DetailState()
    object Refreshing : DetailState()
    data class Content(
        val election: Election,
        val arrayList: ArrayList<Map<String, Any>>,
        val keys: List<String>,
        val resources: List<Int>
    ) : DetailState()
    data class Error(val error: String? = null) : DetailState()
}
