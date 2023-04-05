package com.n27.core.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.R
import com.n27.test.assertions.ListAssertions.assertListTexts
import com.n27.test.assertions.ListAssertions.assertListTextsWithDifferentPositions
import com.n27.test.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.generators.getElection
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Test
import java.text.NumberFormat.getIntegerInstance

class DetailActivityUITest {

    private val congressElection = getElection()
    private val senateElection = getElection(chamberName = KEY_SENATE)
    private val mockWebServer = MockWebServer()

    @Test
    fun checkElectionDetailElements() {
        launchActivity()

        assertToolbarTitle("${congressElection.chamberName} (${congressElection.place} ${congressElection.date})")

        with(congressElection.results[0]) {
            assertListTexts(
                listId = R.id.list_activity_detail,
                position = 0,
                texts = listOf(
                    party.name,
                    getIntegerInstance().format(votes).toString(),
                    getIntegerInstance().format(elects).toString()
                )
            )
        }
    }

    @Test
    fun checkElectionDetailError() {
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.start(8080)
        launchActivity("01")

        assertDisplayed(R.id.error_animation_activity_detail)
        waitUntil { assertDisplayed("Oops! Something went wrong.") }
    }

    @Test
    fun checkRegionalElectionContent() {
        mockWebServer.enqueue(MockResponse().setBody(DetailActivityResponses.regionalElection))
        mockWebServer.start(8080)
        launchActivity("01")

        assertToolbarTitle("Parlamento (Aragón 2019)")
        checkRegionalContent()

        clickOn(R.id.action_reload)

        assertDisplayed(R.id.progress_bar_activity_detail)
        checkRegionalContent()
    }

    private fun checkRegionalContent() {
        assertListTexts(
            listId = R.id.list_activity_detail,
            position = 0,
            texts = listOf(
                "PSOE",
                "203,933",
                "24"
            )
        )
    }

    // TODO: Test local election.

    @Test
    fun clickOnFloatingButtonShouldOpenDialog() {
        launchActivity()

        clickOn(R.id.more_info_button_activity_detail)

        with(congressElection) {
            assertDisplayed(R.id.title_dialog_detail, "$chamberName $place $date")
            assertListTextsWithDifferentPositions(R.id.list_dialog_detail, listOf(
                "$scrutinized %",
                getIntegerInstance().format(totalElects).toString(),
                getIntegerInstance().format(validVotes).toString(),
                getIntegerInstance().format(abstentions).toString(),
                getIntegerInstance().format(nullVotes).toString(),
                getIntegerInstance().format(blankVotes).toString(),
            ))
        }
    }

    private fun launchActivity(electionId: String? = null) = ActivityScenario.launch<DetailActivity>(
        Intent(getInstrumentation().targetContext, DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION, congressElection)
            putExtra(KEY_SENATE_ELECTION, senateElection)
            putExtra(KEY_ELECTION_ID, electionId)
        }
    )

    @After
    fun teardown() { mockWebServer.shutdown() }
}
