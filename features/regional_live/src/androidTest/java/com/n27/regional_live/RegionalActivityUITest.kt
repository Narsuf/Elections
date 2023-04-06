package com.n27.regional_live

import androidx.test.core.app.ActivityScenario.launch
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.core.presentation.detail.DetailActivity
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.intents.intents
import com.n27.test.intents.verifyIntent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test

class RegionalActivityUITest {

    private val mockWebServer = MockWebServer()

    @Before
    fun setup() {
        for (i in 0..15) mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(MockResponse().setBody(RegionalActivityResponses.regionalElection))
        mockWebServer.enqueue(MockResponse().setBody(RegionalActivityResponses.localElection))
        mockWebServer.start(8080)
    }

    @Test
    fun checkRegionalContent() {
        launchActivity()

        waitUntil { assertDisplayed(R.id.recycler_fragment_regionals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_regionals, 0, R.id.name_card_regional_election, "Aragón")

        intents {
            clickOn("Aragón")
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    @Test
    fun checkLocalContent() {
        launchActivity()

        clickOn("Locals")
        waitUntil { assertDisplayed(R.id.recycler_fragment_locals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 0, R.id.region_name_card_local_election, "Andalucía")
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 9, R.id.region_name_card_local_election, "Extremadura")
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 18, R.id.region_name_card_local_election, "Melilla")

        // Open and check dialog
        clickOn("La Rioja")
        assertDisplayed("La Rioja")
        assertDisplayed("Ábalos")

        intents {
            clickOn("SHOW RESULTS")
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    @After
    fun teardown() { mockWebServer.shutdown() }
}
