package org.n27.elections.presentation.detail

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import org.n27.elections.R
import org.n27.elections.data.utils.getElection
import org.n27.elections.presentation.common.Constants
import org.n27.elections.presentation.common.Constants.KEY_SENATE
import org.n27.elections.utils.assertions.ListAssertions.assertListTexts
import org.n27.elections.utils.assertions.ListAssertions.assertListTextsWithDifferentPositions
import org.n27.elections.utils.assertions.ToolbarAssertions.assertToolbarTitle
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
