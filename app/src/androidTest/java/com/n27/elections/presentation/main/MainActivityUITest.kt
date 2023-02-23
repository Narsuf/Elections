package com.n27.elections.presentation.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.n27.elections.R
import com.n27.elections.presentation.detail.DetailActivity
import com.n27.elections.utils.actions.SpanActions.clickClickableSpan
import com.n27.elections.utils.assertions.ToolbarAssertions.assertToolbarTitle
import com.n27.elections.utils.intents.intents
import com.n27.elections.utils.intents.mockIntent
import com.n27.elections.utils.intents.verifyBrowserOpened
import com.n27.elections.utils.intents.verifyIntent
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.lang.Thread.sleep

class MainActivityUITest {

    private val mockWebServer = MockWebServer()
    private val json = "{\n" +
            "  \"elections\": [\n" +
            "    {\n" +
            "      \"id\": 3,\n" +
            "      \"date\": \"2015\",\n" +
            "      \"name\": \"Generales\",\n" +
            "      \"place\": \"Espa\\u00f1a\",\n" +
            "      \"chamberName\": \"Congreso\",\n" +
            "      \"totalElects\": 350,\n" +
            "      \"scrutinized\": 100.0,\n" +
            "      \"validVotes\": 25349824,\n" +
            "      \"abstentions\": 9280429,\n" +
            "      \"blankVotes\": 187766,\n" +
            "      \"nullVotes\": 226994,\n" +
            "      \"results\": [\n" +
            "        {\n" +
            "          \"id\": 17,\n" +
            "          \"elects\": 123,\n" +
            "          \"votes\": 7215530,\n" +
            "          \"election\": 3,\n" +
            "          \"party\": {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"PP\",\n" +
            "            \"color\": \"1D84CE\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 4,\n" +
            "      \"date\": \"2015\",\n" +
            "      \"name\": \"Generales\",\n" +
            "      \"place\": \"Espa\\u00f1a\",\n" +
            "      \"chamberName\": \"Senado\",\n" +
            "      \"totalElects\": 208,\n" +
            "      \"scrutinized\": 99.99,\n" +
            "      \"validVotes\": 25752839,\n" +
            "      \"abstentions\": 8120062,\n" +
            "      \"blankVotes\": 519409,\n" +
            "      \"nullVotes\": 580989,\n" +
            "      \"results\": [\n" +
            "        {\n" +
            "          \"id\": 18,\n" +
            "          \"elects\": 101,\n" +
            "          \"votes\": 0,\n" +
            "          \"party\": {\n" +
            "            \"id\": 1,\n" +
            "            \"name\": \"PP\",\n" +
            "            \"color\": \"1D84CE\"\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.enqueue(MockResponse().setBody(json))
        mockWebServer.start(8080)
    }

    @After
    @Throws(IOException::class)
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun clickOnElectionShouldNavigateToDetail() {
        launchActivity()

        intents {
            mockIntent()

            val link = "El País"
            val dialogDescription = "This app does not represent any government entity. " +
                    "The data of the results is retrieved from the Spanish newspaper $link."

            onView(withText(dialogDescription)).perform(clickClickableSpan(link))

            verifyBrowserOpened("https://resultados.elpais.com/elecciones/generales.html")
        }

        clickOn("CLOSE")
        sleep(5000)
        assertToolbarTitle("Elections")
        assertNotDisplayed(R.id.error_animation)
        assertNotDisplayed(R.id.loading_animation)
        assertDisplayed(R.id.recyclerView)
        intents {
            clickListItem(R.id.recyclerView, 0)
            verifyIntent(DetailActivity::class.java.name)
        }
    }

    private fun launchActivity() = ActivityScenario.launch(MainActivity::class.java)
}
