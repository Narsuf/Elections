package com.jorgedguezm.elections.presentation.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertNotDisplayed
import com.adevinta.android.barista.interaction.BaristaListInteractions.clickListItem
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.presentation.detail.DetailActivity
import com.jorgedguezm.elections.utils.assertions.ToolbarAssertions.assertToolbarTitle
import com.jorgedguezm.elections.utils.intents.intents
import org.hamcrest.Matchers.allOf
import org.junit.Test
import java.lang.Thread.sleep

class MainActivityUITest {

    @Test
    fun clickOnElectionShouldNavigateToDetail() {
        launchActivity()

        assertToolbarTitle("Elections")
        sleep(3000)
        assertNotDisplayed(R.id.error_animation)
        assertNotDisplayed(R.id.loading_animation)
        assertDisplayed(R.id.recyclerView)
        intents {
            clickListItem(R.id.recyclerView, 0)
            intended(allOf(hasComponent(DetailActivity::class.java.name)))
        }
    }

    private fun launchActivity() = ActivityScenario.launch(MainActivity::class.java)
}
