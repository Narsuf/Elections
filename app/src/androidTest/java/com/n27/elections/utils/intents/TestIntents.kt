package com.n27.elections.utils.intents

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import androidx.test.espresso.intent.Intents.init
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.Intents.release
import org.hamcrest.Matcher

fun intents(func: TestIntents.() -> Unit) {
    TestIntents().apply {
        init()

        try {
            func()
        } finally {
            release()
        }
    }
}

class TestIntents internal constructor() {

    fun mockIntent(matcher: Matcher<Intent>, result: ActivityResult) {
        intending(matcher).respondWith(result)
    }
}
