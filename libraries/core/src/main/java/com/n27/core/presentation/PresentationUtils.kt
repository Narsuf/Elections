package com.n27.core.presentation

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.view.View
import com.google.firebase.analytics.ktx.ParametersBuilder
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.n27.core.BuildConfig

open class PresentationUtils {

    inline fun track(eventName: String, crossinline block: ParametersBuilder.() -> Unit = {}) {
        if (!BuildConfig.DEBUG) Firebase.analytics.logEvent(eventName, block)
    }

    fun trackLink(link: String) {
        track("link_opened") { param("link", link) }
    }

    fun getTextWithLink(text: String, link: String, onClick: () -> Unit) = SpannableString(text).apply {
        val start = text.indexOf(link)
        val end = start + link.length
        val flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        val click = object : ClickableSpan() {
            override fun onClick(widget: View) { onClick() }
        }

        setSpan(click, start, end, flag)
    }
}
