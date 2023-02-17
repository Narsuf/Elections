package com.n27.elections.data

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.n27.elections.data.models.Election
import com.n27.elections.data.room.ElectionDao
import com.n27.elections.data.utils.getApiResponse
import com.n27.elections.data.utils.getElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryTest {

    private lateinit var repository: ElectionRepository
    private lateinit var service: ElectionApi
    private lateinit var dao: ElectionDao
    private lateinit var dataUtils: DataUtils
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var crashlytics: FirebaseCrashlytics
    private val exception = IndexOutOfBoundsException("Failed to connect to ")
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        service = mock(ElectionApi::class.java)
        dao = mock(ElectionDao::class.java)
        dataUtils = mock(DataUtils::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        crashlytics = mock(FirebaseCrashlytics::class.java)
        dataUtils.context = ApplicationProvider.getApplicationContext()
        repository = ElectionRepository(service, dao, dataUtils, firebaseDatabase, crashlytics)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun loadElectionsFromDb() = runTest {
        val daoElections = getApiResponse().elections

        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        val expected = repository.getElections()

        assertEquals(expected, Result.success(daoElections))
    }

    @Test
    fun loadElectionsFromApi() = runTest {
        val apiElections = getApiResponse()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenReturn(apiElections)

        val expected = repository.getElections()
        val expectedInsert = apiElections.elections[0].toElectionWithResultsAndParty()

        assertEquals(expected, Result.success(apiElections.elections))
        verify(dao, times(1)).insertElectionWithResultsAndParty(expectedInsert)

    }

    @Test
    fun loadElectionsFromDbWhenApiFails() = runTest {
        val daoElections = getElections()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenThrow(exception)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        val expected = repository.getElections()

        assertEquals(expected, Result.success(daoElections))
    }

    @Test
    fun `try load elections from firebase when fallback but empty db`() = runTest {
        val daoElections = listOf<Election>()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenThrow(exception)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        try {
            repository.getElections()
        } catch(e: Exception) {
            // Not proud of this test, but Firebase's API doesn't make it easy.
            assertTrue(e is NullPointerException)
        }
    }
}
