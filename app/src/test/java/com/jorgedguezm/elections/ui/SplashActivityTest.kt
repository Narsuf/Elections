package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import org.robolectric.RobolectricTestRunner

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class SplashActivityTest {

    @Test
    fun isConnectedToInternet() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.utils.isConnectedToInternet())
                sleep(1000)
            }
        }
    }
}