package com.n27.regional_live.presentation.locals

import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.R
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.presentation.RegionalLiveActivity
import com.n27.regional_live.presentation.locals.adapters.LocalsCardAdapter
import com.n27.regional_live.presentation.locals.entities.LocalsState.Error
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocalsFragmentTest {

    @Test
    fun checkContentViewState() {
        launchActivity().navigateToLocals().onActivity { activity ->
            with(activity.getFragment()) {
                val state = getLocalsContent()

                renderState(state)

                binding.assertVisibilities(content = true)

                val recyclerAdapter = binding.recyclerFragmentLocals.adapter!! as LocalsCardAdapter
                assertTrue(state.regions.containsAll(recyclerAdapter.regions))
                assertEquals(recyclerAdapter.onRegionClicked, ::showSelectionDialog)
            }
        }
    }

    @Test
    fun checkErrorViewState() {
        launchActivity().navigateToLocals().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(Error())
                binding.assertVisibilities(error = true)
            }
        }
    }

    private fun FragmentLocalsBinding.assertVisibilities(
        error: Boolean = false,
        content: Boolean = false
    ) {
        assertEquals(recyclerFragmentLocals.isVisible, content)
        assertEquals(errorFragmentLocals.isVisible, error)
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    private fun ActivityScenario<RegionalLiveActivity>.navigateToLocals() = onActivity {
        it.findNavController(R.id.nav_host_fragment_activity_regional_live)
            .navigate(R.id.navigation_locals)
    }

    private fun RegionalLiveActivity.getFragment() = supportFragmentManager
        .fragments[0]
        .childFragmentManager
        .fragments[0] as LocalsFragment
}
