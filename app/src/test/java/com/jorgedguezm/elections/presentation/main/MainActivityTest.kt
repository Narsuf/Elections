package com.jorgedguezm.elections.presentation.main

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun checkSuccessViewState() {
        val state = getMainSuccess()

        launchActivity().use { scenario ->
            scenario.onActivity { activity ->
                val binding = activity.binding

                assertEquals(binding.toolbar.title, activity.resources.getString(R.string.app_name))

                activity.renderState(state)

                val adapter = binding.recyclerView.adapter!! as GeneralCardAdapter
                assertEquals(adapter.elections, state.elections)
                assertEquals(adapter.onElectionClicked, state.onElectionClicked)
            }
        }
    }

    private fun launchActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)
}
