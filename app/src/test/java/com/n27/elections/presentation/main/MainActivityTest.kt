package com.n27.elections.presentation.main

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.R
import com.n27.elections.presentation.main.adapters.GeneralCardAdapter
import com.n27.elections.presentation.main.entities.MainState.Error
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun checkSuccessViewState() {
        val state = getMainSuccess()

        launchActivity().onActivity { activity ->
            val binding = activity.binding

            assertEquals(binding.toolbar.title, activity.resources.getString(R.string.app_name))

            activity.renderState(state)

            val recyclerAdapter = binding.recyclerView.adapter!! as GeneralCardAdapter
            assertTrue(state.elections.containsAll(recyclerAdapter.congressElections))
            assertTrue(state.elections.containsAll(recyclerAdapter.senateElections))
            assertEquals(recyclerAdapter.onElectionClicked, state.onElectionClicked)
        }
    }

    @Test
    fun checkLoadingViewState() {
        launchActivity().onActivity { activity ->
            // Loading is triggered automatically.
            assertTrue(activity.binding.loadingAnimation.isVisible)
        }
    }

    @Test
    fun checkGenericErrorViewState() {
        val state = getMainError()
        checkError(state)
    }

    @Test
    fun checkNoInternetConnectionErrorViewState() {
        val state = getMainError(NO_INTERNET_CONNECTION)
        checkError(state)
    }

    private fun checkError(state: Error) {
        launchActivity().onActivity { activity ->
            val binding = activity.binding

            assertTrue(binding.loadingAnimation.isVisible)
            assertFalse(binding.errorAnimation.isVisible)

            activity.renderState(state)

            assertFalse(binding.loadingAnimation.isVisible)
            assertTrue(binding.errorAnimation.isVisible)
        }
    }

    private fun launchActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)
}

