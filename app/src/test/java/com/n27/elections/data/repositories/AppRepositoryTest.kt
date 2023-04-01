package com.n27.elections.data.repositories

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.n27.elections.ElectionsApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class AppRepositoryTest {

    private lateinit var repository: AppRepository
    private lateinit var sharedPreferences: SharedPreferences
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        sharedPreferences = ApplicationProvider.getApplicationContext<ElectionsApplication>()
            .getSharedPreferences("prefs", MODE_PRIVATE)
        repository = AppRepository(sharedPreferences)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun isFirstLaunch() = runTest {
        assertTrue(repository.isFirstLaunch())

        repository.saveFirstLaunchFlag()

        assertFalse(repository.isFirstLaunch())
    }
}
