package com.n27.regional_live

import androidx.test.core.app.ActivityScenario.launch
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.presentation.RegionalLiveActivity
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.intents.intents
import com.n27.test.intents.verifyIntent
import com.n27.test.jsons.ElDiarioApiResponses
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import java.lang.Thread.sleep

class RegionalActivityUiTest {

    private val mockWebServer = MockWebServer()

    private fun prepareSuccessfulResponses() {
        for (i in 0..15) mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.regionalElection))
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.regionalParties))
    }

    @Test
    fun checkRegionalContent() {
        prepareSuccessfulResponses()
        mockWebServer.start(8080)
        launchActivity()

        waitUntil { assertDisplayed(R.id.recycler_fragment_regionals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_regionals, 0, R.id.name_card_regional_election, "C. Valenciana")

        intents {
            clickOn("C. Valenciana")
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    @Test
    fun checkLocalSuccess() {
        prepareSuccessfulResponses()
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.localElection))
        mockWebServer.enqueue(MockResponse().setBody(ElDiarioApiResponses.localParties))
        mockWebServer.start(8080)
        launchActivity()

        checkLocalContent()
        checkDialogContent()
        intents {
            clickOn("SHOW RESULTS")
            sleep(1000)
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    @Test
    fun checkLocalError() {
        prepareSuccessfulResponses()
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.start(8080)
        launchActivity()

        checkLocalContent()
        checkDialogContent()
        clickOn("SHOW RESULTS")
        waitUntil { assertDisplayed("Oops! Something went wrong.") }
    }

    private fun checkLocalContent() {
        waitUntil { assertDisplayed(R.id.recycler_fragment_regionals) }
        clickOn(R.id.navigation_locals)
        waitUntil { assertDisplayed(R.id.recycler_fragment_locals) }
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 0, R.id.region_name_card_local_election, "Andalucía")
        assertDisplayedAtPosition(R.id.recycler_fragment_locals, 5, R.id.region_name_card_local_election, "Cantabria")
    }

    private fun checkDialogContent() {
        clickOn("Andalucía")
        waitUntil { assertDisplayed("Almería") }
        waitUntil { assertDisplayed("Abla") }
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    @After
    fun teardown() { mockWebServer.shutdown() }
}
