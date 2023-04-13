package com.n27.core.presentation.detail.models

import com.n27.core.data.models.Election

sealed class DetailContentState {

    object Empty : DetailContentState()

    data class WithData(
        val election: Election,
        val arrayList: ArrayList<Map<String, Any>>,
        val keys: List<String>,
        val resources: List<Int>
    ) : DetailContentState()
}
