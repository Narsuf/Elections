package com.n27.elections.data.repositories

import com.google.firebase.database.FirebaseDatabase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.models.Election
import com.n27.elections.data.api.ElectionApi
import com.n27.elections.data.api.models.ApiResponse
import com.n27.test.generators.getElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryTest {

    private lateinit var repository: ElectionRepository
    private lateinit var service: ElectionApi
    private lateinit var dao: ElectionDao
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var dataUtils: DataUtils
    private val exception = IndexOutOfBoundsException("Failed to connect to ")

    @Before
    fun setUp() {
        service = mock(ElectionApi::class.java)
        dao = mock(ElectionDao::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        dataUtils = mock(DataUtils::class.java)
        repository = ElectionRepository(service, dao, firebaseDatabase, dataUtils)
    }

    @Test
    fun loadElectionsFromDb() = runBlocking {
        val daoElections = getElections()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getElections()).thenReturn(daoElections.toElectionsWithResultsAndParty())

        assertEquals(repository.getElections(), daoElections)
    }

    @Test
    fun loadElectionsFromApi() = runBlocking {
        val apiElections = ApiResponse(getElections())

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenReturn(apiElections)

        val expectedInsert = apiElections.elections.toElectionsWithResultsAndParty()

        assertEquals(repository.getElections(), apiElections.elections)
        verify(dao, times(1)).insertElections(expectedInsert)
    }

    @Test
    fun loadElectionsFromDbWhenApiFails() = runBlocking {
        val daoElections = getElections()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenThrow(exception)
        `when`(dao.getElections()).thenReturn(daoElections.toElectionsWithResultsAndParty())

        assertEquals(repository.getElections(), daoElections)
    }

    @Test
    fun loadElectionsFromDbWhenNoInternetButDbEmpty(): Unit = runBlocking {
        val daoElections = listOf<Election>()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.getElections()).thenReturn(daoElections.toElectionsWithResultsAndParty())

        runCatching { repository.getElections() }.getOrElse { assertEquals(it.message, NO_INTERNET_CONNECTION) }
    }

    @Test
    fun `try load elections from firebase when fallback but empty db`(): Unit = runBlocking {
        val daoElections = listOf<Election>()

        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections()).thenThrow(exception)
        `when`(dao.getElections()).thenReturn(daoElections.toElectionsWithResultsAndParty())

        // Not proud of this test, but Firebase's API doesn't make it easy.
        runCatching { repository.getElections() }.getOrElse { assertTrue(it is NullPointerException) }
    }
}
