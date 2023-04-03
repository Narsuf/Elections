package com.n27.core.presentation.detail

import com.n27.core.presentation.detail.DetailState.Failure

fun getDetailFailure(error: String? = null) = Failure(error)
