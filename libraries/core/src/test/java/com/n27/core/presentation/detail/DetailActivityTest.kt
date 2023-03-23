package com.n27.core.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.test.generators.getElection
import junit.framework.TestCase.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DetailActivityTest {

    private val congressElection = getElection()
    private val senateElection = getElection(KEY_SENATE)

    @Test
    fun launchDetailActivity() {
        launchActivity().onActivity { activity ->
            with(activity) {
                // Congress results loaded
                checkCongress()

                // Swap option clicked
                onOptionsItemSelected(binding.toolbar.menu.getItem(0))
                checkSenate()

                // Swap option clicked again
                onOptionsItemSelected(binding.toolbar.menu.getItem(0))
                checkCongress()

                // Check highlight function
                binding.listView.onItemClickListener?.onItemClick(null, null, 0, 0)
                assertTrue(binding.pieChart.highlighted.isNotEmpty())

                // Still highlighted
                countDownTimer.onTick(1)
                assertTrue(binding.pieChart.highlighted.isNotEmpty())

                // Not highlighted anymore
                countDownTimer.onFinish()
                assertNull(binding.pieChart.highlighted)
            }
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
            putExtra(KEY_ELECTION, congressElection)
            putExtra(KEY_SENATE_ELECTION, senateElection)
        }
    )
}
