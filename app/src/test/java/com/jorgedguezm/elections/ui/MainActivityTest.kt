package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.view.ui.main.MainActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import kotlinx.android.synthetic.main.fragment_main.*

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun launchMainActivityAndPerformClickOnFirstCard() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val firstFragment = activity.supportFragmentManager.fragments[0]

                // Click first card
                firstFragment.recyclerView.layoutManager?.getChildAt(0)?.performClick()
            }
        }
    }
}