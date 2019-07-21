package com.jorgedguezm.elections

import org.robolectric.RobolectricTestRunner

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.ui.SplashActivity

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertTrue

@RunWith(RobolectricTestRunner::class)
class SplashActivityTest {

    @Test
    fun isConnectedToInternet() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.utils.isConnectedToInternet())
            }
        }
    }
}