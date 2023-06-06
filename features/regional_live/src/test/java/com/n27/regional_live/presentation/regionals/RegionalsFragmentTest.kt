package com.n27.regional_live.presentation.regionals

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.presentation.RegionalLiveActivity
import com.n27.regional_live.presentation.regionals.adapters.RegionalCardAdapter
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Error
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RegionalsFragmentTest {

    @Test
    fun checkContentViewState() {
        launchActivity().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(getRegionalsContent())
                binding.assertVisibilities(content = true)
            }
        }
    }

    @Test
    fun checkErrorViewState() {
        launchActivity().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(Error())
                binding.assertVisibilities(error = true)
            }
        }
    }

    @Test
    fun checkWithData() {
        val state = getRegionalsContent()

        launchActivity().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(state)

                binding.assertVisibilities(content = true)

                val recyclerAdapter = binding.recyclerFragmentRegionals.adapter!! as RegionalCardAdapter
                assertTrue(state.elections.items.containsAll(recyclerAdapter.elections.items))
                assertEquals(recyclerAdapter.onElectionClicked, ::navigateToDetail)
            }
        }
    }

    private fun FragmentRegionalsBinding.assertVisibilities(
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) {
        assertEquals(regionalsLoadingAnimation.isVisible, loading)
        assertEquals(regionalsErrorAnimation.isVisible, error)
        assertEquals(recyclerFragmentRegionals.isVisible, content)
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    private fun RegionalLiveActivity.getFragment() = supportFragmentManager
        .fragments[0]
        .childFragmentManager
        .fragments[0] as RegionalsFragment
}
