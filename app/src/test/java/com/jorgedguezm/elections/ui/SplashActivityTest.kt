package com.jorgedguezm.elections.ui

import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowNetworkInfo

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

import androidx.test.core.app.ActivityScenario

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

import java.lang.Thread.sleep

@RunWith(RobolectricTestRunner::class)
class SplashActivityTest {

    @Test
    fun isConnectedToInternet() {
        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertTrue(activity.utils.isConnectedToInternet())
                assertNotNull(activity.electionsViewModel.electionsResult().value)
                sleep(1000)
            }
        }
    }

    @Test
    fun notConnectedToInternet() {
        val connectivityManager = RuntimeEnvironment.systemContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val shadowConnectivityManager = shadowOf(connectivityManager)
        val networkInfo = ShadowNetworkInfo.newInstance(NetworkInfo.DetailedState.DISCONNECTED,
                0, 0, false, NetworkInfo.State.DISCONNECTED)

        shadowConnectivityManager.setActiveNetworkInfo(networkInfo)

        ActivityScenario.launch(SplashActivity::class.java).use { scenario ->
            scenario.onActivity { activity ->
                assertFalse(activity.utils.isConnectedToInternet())
            }
        }
    }
}