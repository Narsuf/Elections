package com.n27.core.presentation.detail

import android.content.Intent
import androidx.core.view.isVisible
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_GENERAL_LIVE_ELECTION
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.databinding.ActivityDetailBinding
import com.n27.core.domain.election.Election
import com.n27.core.presentation.detail.entities.DetailState.Loading
import com.n27.test.generators.getElection
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailActivityTest {

    private val congressElection = getElection()
    private val senateElection = getElection(chamberName = KEY_SENATE)

    @Test
    fun checkLoadingViewState() {
        launchActivity().onActivity { activity ->
            with(activity) {
                renderState(Loading)
                binding.assertVisibilities(animation = true)
            }
        }
    }

    @Test
    fun checkErrorViewState() {
        launchActivity(election = null).onActivity { activity ->
            with(activity) {
                renderState(getDetailError())
                binding.assertVisibilities(error = true)
            }
        }
    }

    @Test
    fun checkContentViewState() {
        launchActivity().onActivity { activity ->
            // Content is triggered automatically.
            activity.binding.assertVisibilities(content = true)
        }
    }

    private fun ActivityDetailBinding.assertVisibilities(
        animation: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) {
        assertEquals(loadingAnimationActivityDetail.isVisible, animation)
        assertEquals(swipeActivityDetail.isRefreshing, loading)
        assertEquals(errorAnimationActivityDetail.isVisible, error)
        assertEquals(contentActivityDetail.isVisible, content)
    }

    @Test
    fun checkSwap() {
        launchActivity(senateElection = senateElection).onActivity { activity ->
            with(activity) {
                val swap = binding.toolbarActivityDetail.menu.getItem(0)
                assertTrue(swap.isVisible)

                // Congress results loaded
                checkCongress()

                // Swap option clicked
                onOptionsItemSelected(swap)
                checkSenate()

                // Swap option clicked again
                onOptionsItemSelected(swap)
                checkCongress()
            }
        }
    }

    private fun DetailActivity.checkCongress() {
        assertTrue(binding.toolbarActivityDetail.title.contains("Congreso"))
        assertEquals(congressElection.id, currentElection?.id)
    }

    private fun DetailActivity.checkSenate() {
        assertTrue(binding.toolbarActivityDetail.title.contains("Senado"))
        assertEquals(senateElection.id, currentElection?.id)
    }

    @Test
    fun swapNotVisibleWhenNoSenateElection() {
        launchActivity().onActivity { activity ->
            val swap = activity.binding.toolbarActivityDetail.menu.getItem(0)
            assertFalse(swap.isVisible)
        }
    }

    @Test
    fun swapVisibleWhenNoSenateElectionButLive() {
        launchActivity(isLiveGeneralElection = true).onActivity { activity ->
            val swap = activity.binding.toolbarActivityDetail.menu.getItem(0)
            assertTrue(swap.isVisible)
        }
    }

    @Test
    fun checkRefresh() {
        launchActivity(liveElectionId = "01").onActivity { activity ->
            val refresh = activity.binding.swipeActivityDetail
            assertTrue(refresh.isEnabled)
        }
    }

    @Test
    fun `refresh not enabled when senate election not null`() {
        launchActivity(senateElection = getElection()).onActivity { activity ->
            val refresh = activity.binding.swipeActivityDetail
            assertFalse(refresh.isEnabled)
        }
    }

    @Test
    fun checkHighlight() {
        launchActivity().onActivity { activity ->
            with(activity) {
                val pieChart = binding.pieChartActivityDetail

                // Check highlight function
                binding.listActivityDetail.getChildAt(0).performClick()
                assertTrue(pieChart.highlighted.isNotEmpty())

                // Still highlighted
                countDownTimer.onTick(1)
                assertTrue(pieChart.highlighted.isNotEmpty())

                // Not highlighted anymore
                countDownTimer.onFinish()
                assertNull(pieChart.highlighted)
            }
        }
    }

    private fun launchActivity(
        election: Election? = congressElection,
        senateElection: Election? = null,
        liveElectionId: String? = null,
        isLiveGeneralElection: Boolean = false
    ) = ActivityScenario.launch<DetailActivity>(
        Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION, election)
            putExtra(KEY_SENATE_ELECTION, senateElection)
            putExtra(KEY_ELECTION_ID, liveElectionId)
            putExtra(KEY_GENERAL_LIVE_ELECTION, isLiveGeneralElection)
        }
    )
}
