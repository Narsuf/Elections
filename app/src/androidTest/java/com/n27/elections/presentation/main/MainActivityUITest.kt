package com.n27.elections.presentation.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.R
import com.n27.elections.presentation.MainActivity
import com.n27.test.actions.SpanActions.clickClickableSpan
import com.n27.test.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.test.conditions.instructions.waitUntil
import com.n27.test.intents.intents
import com.n27.test.intents.mockIntent
import com.n27.test.intents.verifyBrowserOpened
import com.n27.test.intents.verifyIntent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class MainActivityUITest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.enqueue(MockResponse().setBody(MainActivityResponses.elections))
        mockWebServer.start(8080)
    }

    @Test
    fun clickOnElectionShouldNavigateToDetail() {
        launchActivity()

        intents {
            mockIntent()

            val link = "El País"
            val dialogDescription = "This app does not represent any government entity. " +
                    "The data of the results is retrieved from the Spanish newspaper $link."

            waitUntil { onView(withText(dialogDescription)).perform(clickClickableSpan(link)) }

            verifyBrowserOpened("https://resultados.elpais.com/elecciones/generales.html")
        }

        clickOn("CLOSE")
        assertToolbarTitle("Elections")
        waitUntil { assertNotDisplayed(R.id.loading_animation) }
        assertNotDisplayed(R.id.error_animation)
        assertDisplayed(R.id.recyclerView)

        intents {
            clickListItem(R.id.recyclerView, 0)
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    private fun launchActivity() = ActivityScenario.launch(MainActivity::class.java)


    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
