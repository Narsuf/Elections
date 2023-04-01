package com.n27.elections.presentation

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
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
            with(activity) {
                assertEquals(binding.toolbarActivityMain.title, resources.getString(R.string.app_name))

                renderState(state)

                binding.assertVisibilities(content = true)

                val recyclerAdapter = binding.recyclerActivityMain.adapter!! as GeneralElectionsCardAdapter
                assertTrue(state.elections.containsAll(recyclerAdapter.congressElections))
                assertTrue(state.elections.containsAll(recyclerAdapter.senateElections))
                assertEquals(recyclerAdapter.onElectionClicked, ::navigateToDetail)
            }
        }
    }

    @Test
    fun checkInitialLoadingViewState() {
        launchActivity().onActivity { activity ->
            // Initial loading is triggered automatically.
            activity.binding.assertVisibilities(loading = true)
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

    @Test
    fun checkErrorViewStateWithContentVisible() {
        val success = getMainSuccess()
        val error = getMainError()

        launchActivity().onActivity { activity ->
            with(activity) {
                renderState(success)
                renderState(error)

                binding.assertVisibilities(content = true)
            }
        }
    }

    private fun checkError(state: Error) {
        launchActivity().onActivity { activity ->
            with(activity.binding) {
                assertVisibilities(loading = true)

                activity.renderState(state)

                assertVisibilities(error = true)
            }
        }
    }

    private fun ActivityMainBinding.assertVisibilities(
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) {
        assertEquals(loadingAnimationActivityMain.isVisible, loading)
        assertEquals(errorAnimationActivityMain.isVisible, error)
        assertEquals(recyclerActivityMain.isVisible, content)
    }

    private fun launchActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)
}
