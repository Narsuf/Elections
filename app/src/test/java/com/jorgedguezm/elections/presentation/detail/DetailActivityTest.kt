package com.jorgedguezm.elections.presentation.detail

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.models.ElectionGenerator
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

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val congressElection = ElectionGenerator.generateElection(KEY_CONGRESS)
    private val senateElection = ElectionGenerator.generateElection(KEY_SENATE)
    private val intent = Intent(context, DetailActivity::class.java).apply {
        putExtra(KEY_CONGRESS_ELECTION, congressElection)
        putExtra(KEY_SENATE_ELECTION, senateElection)
    }

    @Test
    fun launchDetailActivity() {
        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(activity.binding.toolbar.title, activity.generateToolbarTitle())

                // Congress results loaded
                assertEquals(congressElection.id, activity.currentElection.id)

                // Congress option clicked
                activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(0))
                assertEquals(congressElection.id, activity.currentElection.id)

                // Senate option clicked
                activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(1))
                assertEquals(activity.binding.toolbar.title, activity.generateToolbarTitle())
                assertEquals(senateElection.id, activity.currentElection.id)

                // Senate option clicked again
                activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(1))
                assertEquals(senateElection.id, activity.currentElection.id)

                // Congress option clicked
                activity.onOptionsItemSelected(activity.binding.toolbar.menu.getItem(0))
                assertEquals(congressElection.id, activity.currentElection.id)

                val fragment = activity.supportFragmentManager.fragments[0] as DetailFragment

                // Check highlight function
                fragment.binding.listView.onItemClickListener?.onItemClick(null, null, 0, 0)
                assertTrue(fragment.binding.pieChart.highlighted.isNotEmpty())

                fragment.countDownTimer.onTick(1)
                assertTrue(fragment.binding.pieChart.highlighted.isNotEmpty())

                fragment.countDownTimer.onFinish()
                assertNull(fragment.binding.pieChart.highlighted)

                fragment.binding.floatingButtonMoreInfo.performClick()
            }
        }
    }
}
