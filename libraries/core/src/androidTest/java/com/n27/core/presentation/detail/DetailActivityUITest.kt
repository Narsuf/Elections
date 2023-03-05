package com.n27.core.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.core.R
import com.n27.core.Constants
import com.n27.core.Constants.KEY_SENATE
import com.n27.test.assertions.ListAssertions.assertListTexts
import com.n27.test.assertions.ListAssertions.assertListTextsWithDifferentPositions
import com.n27.test.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.test.generators.getElection
import org.junit.Test
import java.text.NumberFormat.getIntegerInstance

class DetailActivityUITest {

    private val congressElection = getElection()
    private val senateElection = getElection(KEY_SENATE)

    @Test
    fun checkElectionDetailElements() {
        launchActivity()

        assertToolbarTitle(congressElection.chamberName + " (" + congressElection.date + ")")

        with(congressElection.results[0]) {
            assertListTexts(R.id.list_view, 0, listOf(
                party.name,
                getIntegerInstance().format(votes).toString(),
                getIntegerInstance().format(elects).toString()
            ))
        }
    }

    @Test
    fun clickOnFloatingButtonShouldOpenDialog() {
        launchActivity()

        clickOn(R.id.floating_button_more_info)

        with(congressElection) {
            assertDisplayed(R.id.text_view_title, "$chamberName $place $date")
            assertListTextsWithDifferentPositions(R.id.list_view_general_information, listOf(
                "$scrutinized %",
                getIntegerInstance().format(totalElects).toString(),
                getIntegerInstance().format(validVotes).toString(),
                getIntegerInstance().format(abstentions).toString(),
                getIntegerInstance().format(nullVotes).toString(),
                getIntegerInstance().format(blankVotes).toString(),
            ))
        }
    }

    private fun launchActivity() = ActivityScenario.launch<DetailActivity>(
        Intent(getInstrumentation().targetContext, DetailActivity::class.java).apply {
            putExtra(Constants.KEY_CONGRESS_ELECTION, congressElection)
            putExtra(Constants.KEY_SENATE_ELECTION, senateElection)
        }
    )
}
