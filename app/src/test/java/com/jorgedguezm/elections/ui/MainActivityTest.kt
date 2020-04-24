package com.jorgedguezm.elections.ui

import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.view.ui.main.MainActivity
import com.jorgedguezm.elections.view.ui.main.PlaceholderFragment

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner

import kotlinx.android.synthetic.main.fragment_main.*

import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun launchMainActivityAndPerformClickOnFirstCard() {
        ActivityScenario.launch(MainActivity::class.java).use { scenario ->
            scenario.onActivity { sleep(2000) }
            scenario.onActivity { activity ->
                val firstFragment = activity.supportFragmentManager.fragments[0]
                val adapterSize = firstFragment.recyclerView.adapter?.itemCount
                val responseSize = (firstFragment as PlaceholderFragment).vm
                        .electionsResult.value?.data?.filter { it.chamberName == "Congreso" }?.size

                assertEquals(adapterSize, responseSize)

                firstFragment.recyclerView.layoutManager?.getChildAt(0)?.performClick()
            }
        }
    }
}