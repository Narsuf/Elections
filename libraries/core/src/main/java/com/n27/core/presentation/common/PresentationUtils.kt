package com.n27.core.presentation.common

import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.n27.core.BuildConfig
import java.math.BigDecimal
import java.math.RoundingMode

open class PresentationUtils {

    fun getPercentageWithTwoDecimals(dividend: Int, divisor: Int): String {
        val percentage = (dividend.toDouble() / divisor) * 100
        return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
    }

    inline fun track(eventName: String, crossinline block: ParametersBuilder.() -> Unit = {}) {
        if (!BuildConfig.DEBUG) Firebase.analytics.logEvent(eventName, block)
    }
}
