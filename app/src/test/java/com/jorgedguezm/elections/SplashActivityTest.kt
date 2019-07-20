package com.jorgedguezm.elections

import org.robolectric.Shadows.shadowOf
import org.robolectric.RuntimeEnvironment
import org.robolectric.RobolectricTestRunner

import android.content.Intent
import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.ui.SplashActivity
import com.jorgedguezm.elections.ui.MainActivity

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.assertEquals

@RunWith(RobolectricTestRunner::class)
class SplashActivityTest {

    @Test
    fun splashActivityShouldStartMainActivity() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val expectedIntent = Intent(activity, MainActivity::class.java)
                val actual = shadowOf(RuntimeEnvironment.application).nextStartedActivity
                assertEquals(expectedIntent.component, actual.component)
            }
        }
    }
}