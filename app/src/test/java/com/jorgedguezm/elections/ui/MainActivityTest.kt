package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.ui.main.MainActivity
import com.jorgedguezm.elections.ui.main.PlaceholderFragment
import com.jorgedguezm.elections.ui.main.SectionsPagerAdapter

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchMainActivityAndPerformClickOnFirstCard() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val sectionsPagerAdapter = activity.view_pager.adapter as SectionsPagerAdapter
                assertEquals(sectionsPagerAdapter.count, 3)
                sleep(3000)
            }

            scenario.onActivity { activity ->
                val firstFragment = activity.supportFragmentManager.fragments[0]
                val adapterSize = firstFragment.recyclerView.adapter!!.itemCount
                val responseSize = (firstFragment as PlaceholderFragment).pageViewModel
                        .electionsResult().value!!.size

                assertEquals(adapterSize, responseSize)

                firstFragment.recyclerView.layoutManager?.getChildAt(0)?.performClick()
            }
        }
    }
}