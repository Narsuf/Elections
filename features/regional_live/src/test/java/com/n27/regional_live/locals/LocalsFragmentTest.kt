package com.n27.regional_live.locals

import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.R
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.locals.models.LocalsState
import com.n27.regional_live.locals.models.LocalsState.Error
import com.n27.regional_live.locals.models.LocalsState.Loading
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class LocalsFragmentTest {

    @Test
    fun checkContentViewState() {
        launchActivity().navigateToLocals().onActivity { activity ->
            val fragment = activity.getFragment()
            // Content is automatically triggered.
            assertTrue(fragment.binding.recyclerFragmentLocals.isVisible)
        }
    }

    @Test
    fun checkLoadingViewState() {
        launchActivity().navigateToLocals().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(Loading)
                assertTrue(binding.recyclerFragmentLocals.isVisible)
            }
        }
    }

    @Test
    fun checkErrorViewState() {
        launchActivity().navigateToLocals().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(Error())
                assertFalse(binding.recyclerFragmentLocals.isVisible)
                assertTrue(binding.errorFragmentLocals.isVisible)
            }
        }
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
