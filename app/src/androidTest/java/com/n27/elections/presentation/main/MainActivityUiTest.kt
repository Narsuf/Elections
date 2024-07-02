package com.n27.elections.presentation.main

import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.n27.elections.presentation.MainActivity
import com.n27.test.actions.SpanActions.clickClickableSpan
import com.n27.test.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.intents.intents
import com.n27.test.intents.mockIntent
import com.n27.test.intents.verifyBrowserOpened
import org.junit.Test

class MainActivityUiTest {

    @Test
    fun checkActivity() {
        launchActivity()

        checkFirstLaunchDialog()
        waitUntil { assertToolbarTitle("Elections") }
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

    private fun launchActivity() = launch(MainActivity::class.java)
}