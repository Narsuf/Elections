package com.jorgedguezm.elections.utils.assertions

import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.jorgedguezm.elections.utils.matchers.ToolbarMatchers.withToolbarTitle
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf

object ToolbarAssertions {

    fun assertToolbarTitle(title: String?) {
        onView(instanceOf(Toolbar::class.java)).check(matches(allOf(isDisplayed(), withToolbarTitle(title))))
    }
}
