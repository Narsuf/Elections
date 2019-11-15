package com.jorgedguezm.elections.ui

import android.content.Context
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider

import com.jorgedguezm.elections.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.Constants.KEY_SENATE_ELECTION
import com.jorgedguezm.elections.Utils
import com.jorgedguezm.elections.data.DataUtils
import com.jorgedguezm.elections.ui.detail.DetailActivity

import junit.framework.TestCase.assertEquals

import kotlinx.android.synthetic.main.activity_main.*

import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
class DetailActivityTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val utils = Utils(context)
    private val congressElection = DataUtils.generateElection()
    private val senateElection = DataUtils.generateElection()
    private val intent = Intent(context, DetailActivity::class.java).apply {
        putExtra(KEY_CONGRESS_ELECTION, congressElection)
        putExtra(KEY_SENATE_ELECTION, senateElection)
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchDetailActivity() {
        ActivityScenario.launch<DetailActivity>(intent).use { scenario ->
            scenario.onActivity { activity ->
                assertEquals(activity.toolbar.title, utils.generateToolbarTitle(congressElection))
            }
        }
    }
}