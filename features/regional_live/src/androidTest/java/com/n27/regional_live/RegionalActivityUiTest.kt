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
import org.junit.Test

class RegionalActivityUITest {

    private val mockWebServer = MockWebServer()

    private fun prepareSuccessfulResponses() {
        for (i in 0..15) mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(MockResponse().setBody(RegionalActivityResponses.regionalElection))
    }

    @Test
    fun checkRegionalContent() {
        prepareSuccessfulResponses()
        mockWebServer.start(8080)
        launchActivity()

        waitUntil { assertDisplayed(R.id.recycler_fragment_regionals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_regionals, 0, R.id.name_card_regional_election, "Aragón")

        intents {
            clickOn("Aragón")
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    @Test
    fun checkLocalSuccess() {
        prepareSuccessfulResponses()
        mockWebServer.enqueue(MockResponse().setBody(RegionalActivityResponses.localElection))
        mockWebServer.start(8080)
        launchActivity()

        clickOn("Locals")
        checkLocalContent()

        checkDialogContent()
        intents {
            clickOn("SHOW RESULTS")
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    @Test
    fun checkLocalError() {
        prepareSuccessfulResponses()
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.start(8080)
        launchActivity()

        clickOn("Locals")
        checkLocalContent()

        checkDialogContent()
        clickOn("SHOW RESULTS")
        waitUntil { assertDisplayed("Oops! Something went wrong.") }
    }

    private fun checkDialogContent() {
        clickOn("La Rioja")
        assertDisplayed("La Rioja")
        waitUntil { assertDisplayed("Ábalos") }
    }

    private fun checkLocalContent() {
        waitUntil { assertDisplayed(R.id.recycler_fragment_locals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 0, R.id.region_name_card_local_election, "Andalucía")
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 9, R.id.region_name_card_local_election, "Extremadura")
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 18, R.id.region_name_card_local_election, "Melilla")
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    @After
    fun teardown() { mockWebServer.shutdown() }
}
