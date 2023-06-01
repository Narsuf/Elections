package com.n27.core.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_LOCAL_ELECTION_IDS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.R
import com.n27.core.domain.live.models.LocalElectionIds
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

class DetailActivityUiTest {

    private val congressElection = getElection()
    private val senateElection = getElection(chamberName = KEY_SENATE)
    private val mockWebServer = MockWebServer()

    @Test
    fun checkElectionDetailElements() {
        launchActivity()

        assertToolbarTitle("${congressElection.chamberName} ${congressElection.place} (${congressElection.date})")

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
        mockWebServer.enqueue(MockResponse().setResponseCode(500))
        mockWebServer.start(8080)
        launchActivity("01")

        waitUntil { assertDisplayed(R.id.error_animation_activity_detail) }
        waitUntil { assertDisplayed("Oops! Something went wrong.") }
    }

    @Test
    fun checkRegionalElectionContent() {
        mockWebServer.enqueue(MockResponse().setBody(DetailActivityResponses.regionalElection))
        mockWebServer.enqueue(MockResponse().setBody(DetailActivityResponses.regionalParties))
        mockWebServer.start(8080)
        launchActivity("01")

        waitUntil { assertToolbarTitle(" Andalucía (05/23)") }
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
                "IU",
                "20,310",
                "1"
            )
        )
    }

    @Test
    fun checkLocalElectionContent() {
        mockWebServer.enqueue(MockResponse().setBody(DetailActivityResponses.localElection))
        mockWebServer.enqueue(MockResponse().setBody(DetailActivityResponses.localParties))
        mockWebServer.start(8080)
        launchActivity(electionIds = LocalElectionIds("01", "04", "01"))

        waitUntil { assertToolbarTitle(" Abla (05/23)") }
        assertListTexts(
            listId = R.id.list_activity_detail,
            position = 0,
            texts = listOf(
                "PP",
                "347",
                "4"
            )
        )
    }

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

    private fun launchActivity(
        electionId: String? = null,
        electionIds: LocalElectionIds? = null
    ) = ActivityScenario.launch<DetailActivity>(
        Intent(getInstrumentation().targetContext, DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION, congressElection)
            putExtra(KEY_SENATE_ELECTION, senateElection)
            putExtra(KEY_ELECTION_ID, electionId)
            putExtra(KEY_LOCAL_ELECTION_IDS, electionIds)
        }
    )

    @After
    fun teardown() { mockWebServer.shutdown() }
}
