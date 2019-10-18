package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.ui.main.MainActivity
import com.jorgedguezm.elections.ui.main.SectionsPagerAdapter

import kotlinx.android.synthetic.main.activity_main.*

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchMainActivity() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val sectionsPagerAdapter = activity.view_pager.adapter as SectionsPagerAdapter
                assertEquals(sectionsPagerAdapter.count, 3)
            }
        }
    }
}