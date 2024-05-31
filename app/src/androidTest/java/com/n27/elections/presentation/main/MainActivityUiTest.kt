package com.n27.elections.presentation.main

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.R
import com.n27.elections.presentation.MainActivity
import com.n27.regional_live.presentation.RegionalLiveActivity
import com.n27.test.actions.SpanActions.clickClickableSpan
import com.n27.test.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.intents.intents
import com.n27.test.intents.mockIntent
import com.n27.test.intents.verifyBrowserOpened
import com.n27.test.intents.verifyIntent
import org.junit.Test
import java.lang.Thread.sleep

class MainActivityUiTest {

    @Test
    fun checkActivity() {
        launchActivity()

        checkFirstLaunchDialog()
        checkContent()
        checkNavigation()
    }

    private fun checkFirstLaunchDialog() {
        intents {
            mockIntent()

            val link = "elDiario.es"
            val dialogDescription = "This app does not represent any government entity. " +
                    "The data of the results is retrieved from the Spanish newspaper $link."

            waitUntil("CheckDialog") { onView(withText(dialogDescription)).perform(clickClickableSpan(link)) }

            verifyBrowserOpened("https://elecciones.eldiario.es/")
        }

        clickOn("CLOSE")
    }

    private fun checkContent() {
        waitUntil { assertToolbarTitle("Elections") }
        sleep(4000)
        waitUntil("CheckRecycler") { assertDisplayed(R.id.recycler_activity_main) }
        assertNotDisplayed(R.id.loading_animation_activity_main)
        assertNotDisplayed(R.id.error_animation_activity_main)
    }

    private fun checkNavigation() {
        intents {
            mockIntent()
            clickListItem(R.id.recycler_activity_main, 0)
            verifyIntent(DetailActivity::class.java.name)

            clickOn(R.id.live_elections_button_activity_main)
            verifyIntent(RegionalLiveActivity::class.java.name)
        }
    }

    private fun launchActivity() = launch(MainActivity::class.java)
}
