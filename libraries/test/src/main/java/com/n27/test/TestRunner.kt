package com.n27.test

import android.os.Bundle
import android.provider.Settings.Global.ANIMATOR_DURATION_SCALE
import android.provider.Settings.Global.TRANSITION_ANIMATION_SCALE
import android.provider.Settings.Global.WINDOW_ANIMATION_SCALE
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnitRunner

class TestRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle?) {
        super.onCreate(arguments)
        setAnimations(false)
    }

    override fun finish(resultCode: Int, results: Bundle?) {
        setAnimations(true)
        super.finish(resultCode, results)
    }

    private fun setAnimations(enabled: Boolean) {
        val value = if (enabled) "1.0" else "0.0"
        InstrumentationRegistry.getInstrumentation().uiAutomation.run {
            this.executeShellCommand("settings put global $WINDOW_ANIMATION_SCALE $value")
            this.executeShellCommand("settings put global $TRANSITION_ANIMATION_SCALE $value")
            this.executeShellCommand("settings put global $ANIMATOR_DURATION_SCALE $value")
        }
    }
}