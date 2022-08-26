package com.jorgedguezm.elections.utils.intents

import androidx.test.espresso.intent.Intents

fun intents(func: TestIntents.() -> Unit) {
    TestIntents().apply {
        Intents.init()

        try {
            func()
        } finally {
            Intents.release()
        }
    }
}

class TestIntents internal constructor()
