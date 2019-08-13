package com.jorgedguezm.elections.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.test.core.app.ActivityScenario

import com.jorgedguezm.elections.constants.Constants.Companion.KEY_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.LooperMode

@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    private var elections: List<Election>? = null
    private var parties: List<Party>? = null

    @Before
    fun fillFields() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                elections = activity.electionsViewModel.electionsResult().value
                Thread.sleep(1000)
            }

            scenario.onActivity { activity ->
                parties = activity.electionsViewModel.partiesResult().value
            }

            scenario.close()
        }
    }

    @Test
    @LooperMode(LooperMode.Mode.PAUSED)
    fun launchMainActivity() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                val myIntent = Intent(activity, MainActivity::class.java)
                val electionsParams = Bundle()

                electionsParams.putSerializable(KEY_ELECTIONS, ArrayList(elections))
                electionsParams.putSerializable(KEY_PARTIES, ArrayList(parties))

                myIntent.putExtras(electionsParams)

                ActivityScenario.launch<Activity>(myIntent).use { scenario ->
                    scenario.onActivity { activity ->

                    }
                }
            }
        }
    }
}