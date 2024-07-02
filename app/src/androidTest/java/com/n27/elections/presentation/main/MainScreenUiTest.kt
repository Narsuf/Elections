package com.n27.elections.presentation.main

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NO_RESULTS
import com.n27.core.components.Theme
import com.n27.elections.presentation.MainScreen
import com.n27.elections.presentation.OnElectionClicked
import com.n27.elections.presentation.entities.MainUiState.HasElections
import com.n27.elections.presentation.entities.MainUiState.NoElections
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test

class MainScreenUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val uiState = HasElections(
        congressElections = getElectionList(),
        senateElections = listOf(getElection(chamberName = KEY_SENATE)),
        isLoading = false,
        error = null
    )

    @Test
    fun electionClicked() {
        var electionClicked = false
        val onElectionClicked: OnElectionClicked = { _, _ -> electionClicked = true }

        composeTestRule.setContent {
            Theme {
                MainScreen(
                    uiState,
                    isLiveButtonVisible = false,
                    onPullToRefresh = {},
                    onLiveClicked = {},
                    onElectionClicked
                )
            }
        }

        composeTestRule.onNodeWithText("2015").assertIsDisplayed()

        composeTestRule.onNodeWithText("2015").performClick()
        assertTrue(electionClicked)
    }

    @Test
    fun liveButtonClicked() {
        var liveButtonClicked = false
        val onElectionClicked: OnElectionClicked = { _, _ -> }

        composeTestRule.setContent {
            Theme {
                MainScreen(
                    uiState,
                    isLiveButtonVisible = true,
                    onPullToRefresh = {},
                    onLiveClicked = { liveButtonClicked = true },
                    onElectionClicked
                )
            }
        }

        composeTestRule.onNodeWithContentDescription("Live elections").performClick()
        assertTrue(liveButtonClicked)
    }

    @Test
    fun noElectionsWithError() {
        val onElectionClicked: OnElectionClicked = { _, _ -> }

        composeTestRule.setContent {
            Theme {
                MainScreen(
                    state = NoElections(isLoading = false, error = NO_RESULTS),
                    isLiveButtonVisible = false,
                    onPullToRefresh = {},
                    onLiveClicked = {},
                    onElectionClicked
                )
            }
        }

        composeTestRule.onNodeWithText("Preliminary results not available yet").assertIsDisplayed()
    }

    @Test
    fun hasElectionsWithError() {
        val onElectionClicked: OnElectionClicked = { _, _ -> }

        composeTestRule.setContent {
            Theme {
                MainScreen(
                    state = uiState.copy(error = ""),
                    isLiveButtonVisible = true,
                    onPullToRefresh = {},
                    onLiveClicked = {},
                    onElectionClicked
                )
            }
        }

        composeTestRule.onNodeWithText("Oops! Something went wrong.").assertIsDisplayed()
    }
}