package com.jorgedguezm.elections.ui

import org.robolectric.RobolectricTestRunner

import androidx.test.core.app.ActivityScenario

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotNull

import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class SplashActivityTest {

    @Test
    fun isConnectedToInternet() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.utils.isConnectedToInternet())
                assertNotNull(activity.electionsViewModel.electionsResult().value)
                sleep(1000)
            }
        }
    }
}