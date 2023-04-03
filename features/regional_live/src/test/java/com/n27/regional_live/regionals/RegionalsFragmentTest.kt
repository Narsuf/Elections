package com.n27.regional_live.regionals

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionalsFragmentTest {

    @Test
    fun checkLoadingViewState() {
        launchActivity().onActivity { activity ->
            val fragment = activity.getFragment()
            fragment.binding.assertVisibilities(loading = true)
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

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    private fun RegionalLiveActivity.getFragment() = supportFragmentManager
        .fragments[0]
        .childFragmentManager
        .fragments[0] as RegionalsFragment
}
