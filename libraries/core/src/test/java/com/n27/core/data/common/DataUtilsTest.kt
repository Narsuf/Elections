package com.n27.core.data.common

import android.content.Context
import android.net.ConnectivityManager
import androidx.test.core.app.ApplicationProvider
import com.n27.core.data.DataUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class DataUtilsTest {

    @Test
    fun `isConnectedToInternet() should return false if not connected`() {
        val context = mock(Context::class.java)
        val connectivityManager = mock(ConnectivityManager::class.java)
        val utils = DataUtils(context)

        `when`(connectivityManager.getNetworkCapabilities(any())).thenReturn(null)
        `when`(context.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(connectivityManager)

        assertFalse(utils.isConnectedToInternet())
    }

    @Test
    fun `isConnectedToInternet() should return true if connected`() {
        val utils = DataUtils(ApplicationProvider.getApplicationContext())

        assertTrue(utils.isConnectedToInternet())
    }
}
