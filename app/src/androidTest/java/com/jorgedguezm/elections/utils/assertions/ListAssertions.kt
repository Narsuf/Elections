package com.jorgedguezm.elections.utils.assertions

import androidx.annotation.IdRes
import com.adevinta.android.barista.assertion.BaristaListAssertions.assertDisplayedAtPosition

object ListAssertions {

    fun assertListTexts(@IdRes listId: Int, position: Int, texts: List<String>) {
        texts.forEach { text ->
            assertDisplayedAtPosition(listId, position, text)
        }
    }
}
