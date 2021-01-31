package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.view.ui.main.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import kotlinx.android.synthetic.main.activity_main.*

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun launchMainActivityAndPerformClickOnFirstCard() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                // Swipe
                activity.view_pager.currentItem = 2
            }
        }
    }
}