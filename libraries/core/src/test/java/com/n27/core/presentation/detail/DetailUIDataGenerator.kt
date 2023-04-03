package com.n27.core.presentation.detail

import com.n27.core.presentation.detail.entities.DetailState.Error

fun getDetailFailure(error: String? = null) = Error(error)
