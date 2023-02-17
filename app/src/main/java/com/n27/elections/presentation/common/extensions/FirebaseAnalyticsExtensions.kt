package com.n27.elections.presentation.common.extensions

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

fun FirebaseAnalytics.track(eventName: String, paramId: String, paramValue: String) {
    logEvent(eventName) { param(paramId, paramValue) }
}
