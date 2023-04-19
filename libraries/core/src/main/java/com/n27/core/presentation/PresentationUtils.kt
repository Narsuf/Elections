package com.n27.core.presentation

import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.n27.core.BuildConfig

open class PresentationUtils {

    inline fun track(eventName: String, crossinline block: ParametersBuilder.() -> Unit = {}) {
        if (!BuildConfig.DEBUG) Firebase.analytics.logEvent(eventName, block)
    }
}
