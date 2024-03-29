package com.n27.test.intents

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.times
import androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
import org.hamcrest.core.AllOf.allOf

fun TestIntents.mockIntent() {
    mockIntent(
        matcher = anyIntent(),
        result = ActivityResult(RESULT_OK, Intent())
    )
}

fun TestIntents.verifyBrowserOpened(url: String) {
    intended(
        allOf(
            hasAction(Intent.ACTION_VIEW),
            hasData(url)
        ),
        times(1)
    )
}

fun TestIntents.verifyIntent(className: String) {
    intended(hasComponent(className), times(1))
}
