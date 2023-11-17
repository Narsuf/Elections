package com.n27.elections.data

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.n27.elections.ElectionsApplication
import com.n27.elections.domain.repositories.AppRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppRepositoryTest {

    private lateinit var repository: AppRepository
    private lateinit var sharedPreferences: SharedPreferences

    @Before
    fun setUp() {
        sharedPreferences = ApplicationProvider.getApplicationContext<ElectionsApplication>()
            .getSharedPreferences("prefs", MODE_PRIVATE)
        repository = AppRepositoryImpl(sharedPreferences)
    }

    @Test
    fun isFirstLaunch() = runBlocking {
        assertTrue(repository.isFirstLaunch())

        repository.saveFirstLaunch()

        assertFalse(repository.isFirstLaunch())
    }
}
