package com.n27.regional_live.regionals

import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.R
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionalsFragmentTest {

    @Test
    fun checkInitialLoadingViewState() {
        launchActivity().onActivity { activity ->
            val navHostFragment = activity.supportFragmentManager.fragments[0] as NavHostFragment
            navHostFragment.navController.navigate(R.id.navigation_locals)
            val destination = navHostFragment.navController.currentDestination
            // Initial loading is triggered automatically.
            //fragment.binding.assertVisibilities(loading = true)
        }
    }

    private fun FragmentRegionalsBinding.assertVisibilities(
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) {
        assertEquals(regionalsLoadingAnimation.isVisible, loading)
        assertEquals(regionalsErrorAnimation.isVisible, error)
        assertEquals(regionalsRecyclerView.isVisible, content)
    }

    private fun launchActivity(): ActivityScenario<RegionalLiveActivity> = launch(RegionalLiveActivity::class.java)
}
