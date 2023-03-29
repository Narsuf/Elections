package com.n27.elections.presentation.main

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.R
import com.n27.elections.presentation.MainActivity
import com.n27.elections.presentation.adapters.GeneralElectionsCardAdapter
import com.n27.elections.presentation.entities.MainState.Error
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
            with(activity.binding) {
                assertEquals(toolbarActivityMain.title, activity.resources.getString(R.string.app_name))

                activity.renderState(state)

                val recyclerAdapter = recyclerActivityMain.adapter!! as GeneralElectionsCardAdapter
                assertTrue(state.elections.containsAll(recyclerAdapter.congressElections))
                assertTrue(state.elections.containsAll(recyclerAdapter.senateElections))
                assertEquals(recyclerAdapter.onElectionClicked, state.onElectionClicked)
            }
        }
    }

    @Test
    fun checkLoadingViewState() {
        launchActivity().onActivity { activity ->
            // Loading is triggered automatically.
            assertTrue(activity.binding.loadingAnimationActivityMain.isVisible)
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
            with(activity.binding) {
                assertTrue(loadingAnimationActivityMain.isVisible)
                assertFalse(errorAnimationActivityMain.isVisible)

                activity.renderState(state)

                assertFalse(loadingAnimationActivityMain.isVisible)
                assertTrue(errorAnimationActivityMain.isVisible)
            }
        }
    }

    private fun launchActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)
}

