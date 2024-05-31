package com.n27.elections.presentation

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.adapters.ElectionCardAdapter
import com.n27.elections.presentation.entities.MainState.Error
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @Test
    fun checkLoadingViewState() {
        launchActivity().onActivity {
            it.binding.assertVisibilities(loading = true)
        }
    }

    @Test
    fun checkContentViewState() {
        val state = getMainContent()

        launchActivity().onActivity { activity ->
            with(activity) {
                assertEquals(binding.toolbarActivityMain.title, resources.getString(R.string.app_name))

                renderState(state)

                binding.assertVisibilities(content = true)

                val recyclerAdapter = binding.recyclerActivityMain.adapter!! as ElectionCardAdapter
                assertTrue(state.congressElections.containsAll(recyclerAdapter.congressElections))
                assertTrue(state.senateElections.containsAll(recyclerAdapter.senateElections))
                assertEquals(recyclerAdapter.onElectionClicked, ::navigateToDetail)
            }
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
        assertFalse(swipeActivityMain.isRefreshing)
    }

    private fun launchActivity(): ActivityScenario<MainActivity> = launch(MainActivity::class.java)
}

