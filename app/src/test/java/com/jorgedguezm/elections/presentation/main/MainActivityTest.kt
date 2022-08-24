package com.jorgedguezm.elections.presentation.main

import androidx.test.core.app.ActivityScenario
import com.jorgedguezm.elections.R
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun launchMainActivity() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                activity.binding.toolbar.title = activity.resources.getString(R.string.app_name)
            }
        }
    }
}
