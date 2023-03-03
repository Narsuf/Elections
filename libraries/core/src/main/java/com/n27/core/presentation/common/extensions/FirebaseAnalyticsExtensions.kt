package com.n27.core.presentation.common.extensions

import androidx.multidex.BuildConfig
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.logEvent

inline fun FirebaseAnalytics.track(eventName: String, crossinline block: ParametersBuilder.() -> Unit = {}) {
    if (!BuildConfig.DEBUG) logEvent(eventName, block)
}
