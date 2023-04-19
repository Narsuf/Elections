package com.n27.regional_live.regionals

import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario.launch
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.regionals.adapters.RegionalCardAdapter
import com.n27.regional_live.regionals.models.RegionalsState.Content
import com.n27.regional_live.regionals.models.RegionalsState.Error
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

    @Test
    fun checkContentViewState() {
        launchActivity().onActivity { activity ->
            with(activity.getFragment()) {
                renderState(Content)
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
                renderContentState(state)
                renderState(Content)

                binding.assertVisibilities(content = true)

                val recyclerAdapter = binding.recyclerFragmentRegionals.adapter!! as RegionalCardAdapter
                assertTrue(state.elections.containsAll(recyclerAdapter.elections))
                assertTrue(state.parties.containsAll(recyclerAdapter.parties))
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