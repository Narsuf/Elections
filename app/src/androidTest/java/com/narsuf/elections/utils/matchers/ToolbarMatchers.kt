package com.narsuf.elections.utils.matchers

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.test.espresso.matcher.BoundedMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers

object ToolbarMatchers {

    fun withToolbarTitle(title: CharSequence?) = withToolbarTitle(Matchers.`is`(title))

    fun withToolbarTitle(textMatcher: Matcher<CharSequence?>) =
        object : BoundedMatcher<View, Toolbar>(Toolbar::class.java) {
            override fun matchesSafely(toolbar: Toolbar) = textMatcher.matches(toolbar.title)

            override fun describeTo(description: Description) {
                description.appendText("with toolbar title: ")
                textMatcher.describeTo(description)
            }
        }
}
