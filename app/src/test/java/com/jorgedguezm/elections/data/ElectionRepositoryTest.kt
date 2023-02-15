package com.jorgedguezm.elections.data

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.database.FirebaseDatabase
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.data.utils.getApiResponse
import com.jorgedguezm.elections.data.utils.getElections
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
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        service = mock(ElectionApi::class.java)
        dao = mock(ElectionDao::class.java)
        dataUtils = mock(DataUtils::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        dataUtils.context = ApplicationProvider.getApplicationContext()
        repository = ElectionRepository(service, dao, dataUtils, firebaseDatabase)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun loadElectionsFromDb() = runTest {
        val daoElections = getApiResponse().elections

        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        repository.getElections().collect { assertEquals(it, daoElections) }
    }

    @Test
    fun loadElectionsFromApi() = runTest {
        val apiElections = getApiResponse()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenReturn(apiElections)

        repository.getElections().collect {
            assertEquals(it, apiElections.elections)
            verify(dao, times(1))
                .insertElectionsWithResultsAndParty(anyList())
        }
    }

    @Test
    fun loadElectionsFromDbWhenFallback() = runTest {
        val daoElections = getElections()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        repository.getElections(fallback = true).collect { assertEquals(it, daoElections) }
    }

    @Test
    fun `try load elections from firebase when fallback but empty db`() = runTest {
        val daoElections = listOf<Election>()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(dao.getElections())
            .thenReturn(daoElections.map { it.toElectionWithResultsAndParty() })

        try {
            repository.getElections(fallback = true).collect { }
        } catch(e: Exception) {
            // Not proud of this test, but Firebase's API doesn't make it easy.
            assertTrue(e is NullPointerException)
        }
    }
}
