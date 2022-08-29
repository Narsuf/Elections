package com.jorgedguezm.elections.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.utils.ElectionGenerator
import com.jorgedguezm.elections.presentation.common.Constants.KEY_CONGRESS
import com.jorgedguezm.elections.presentation.common.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.presentation.common.Constants.KEY_SENATE
import com.jorgedguezm.elections.presentation.common.Constants.KEY_SENATE_ELECTION
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailActivityTest {

    private val congressElection = ElectionGenerator.generateElection(KEY_CONGRESS)
    private val senateElection = ElectionGenerator.generateElection(KEY_SENATE)

    @Test
    fun launchDetailActivity() {
        launchActivity().onActivity { activity ->
            // Congress results loaded
            activity.checkCongress()

            // Congress option clicked
            activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(0))
            activity.checkCongress()

            // Senate option clicked
            activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(1))
            activity.checkSenate()

            // Senate option clicked again
            activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(1))
            activity.checkSenate()

            // Congress option clicked
            activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(0))
            activity.checkCongress()

            val fragment = activity.supportFragmentManager.fragments[0] as DetailFragment

            // Check highlight function
            fragment.binding.listView.onItemClickListener?.onItemClick(null, null, 0, 0)
            assertTrue(fragment.binding.pieChart.highlighted.isNotEmpty())

            // Still highlighted
            fragment.countDownTimer.onTick(1)
            assertTrue(fragment.binding.pieChart.highlighted.isNotEmpty())

            // Not highlighted anymore
            fragment.countDownTimer.onFinish()
            assertNull(fragment.binding.pieChart.highlighted)
        }
    }

    private fun DetailActivity.checkCongress() {
        assertTrue(binding.toolbar.title.contains("Congreso"))
        assertEquals(congressElection.id, currentElection.id)
    }

    private fun DetailActivity.checkSenate() {
        assertTrue(binding.toolbar.title.contains("Senado"))
        assertEquals(senateElection.id, currentElection.id)
    }

    private fun launchActivity() = ActivityScenario.launch<DetailActivity>(
        Intent(ApplicationProvider.getApplicationContext(), DetailActivity::class.java).apply {
            putExtra(KEY_CONGRESS_ELECTION, congressElection)
            putExtra(KEY_SENATE_ELECTION, senateElection)
        }
    )
}
