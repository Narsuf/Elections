package com.n27.regional_live

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.lang.Thread.sleep

class RegionalActivityUITest {

    private val mockWebServer = MockWebServer()

    @Before
    @Throws(IOException::class, InterruptedException::class)
    fun setup() {
        mockWebServer.enqueue(MockResponse().setBody(RegionalActivityResponses.regionalElection))
        mockWebServer.start(8080)
    }

    @Test
    fun checkRegionalContent() {
        launchActivity()
        sleep(10000)
    }

    private fun launchActivity() = launch(RegionalLiveActivity::class.java)

    @After
    @Throws(IOException::class)
    fun teardown() { mockWebServer.shutdown() }
}
