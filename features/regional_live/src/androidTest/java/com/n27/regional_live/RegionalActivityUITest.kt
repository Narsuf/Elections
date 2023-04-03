package com.n27.regional_live

import androidx.test.core.app.ActivityScenario.launch
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.n27.test.conditions.instructions.waitUntil
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class RegionalActivityUITest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        for (i in 0..16) {
            mockWebServer.enqueue(
                MockResponse().setBody(RegionalActivityResponses.regionalElection)
            )
        }

        mockWebServer.start(8080)
    }

    @Test
    fun checkRegionalContent() {
        launchActivity()
        waitUntil { assertDisplayed(R.id.regionalsRecyclerView) }
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
