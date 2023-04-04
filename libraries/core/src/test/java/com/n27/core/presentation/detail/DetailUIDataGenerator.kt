package com.n27.core.presentation.detail

import com.n27.core.presentation.detail.models.DetailState.Error

fun getDetailError(error: String? = null) = Error(error)
