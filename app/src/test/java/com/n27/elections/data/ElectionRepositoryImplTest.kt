package com.n27.elections.data

import com.google.firebase.database.FirebaseDatabase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryImplTest {

    private lateinit var repository: ElectionRepositoryImpl
    private lateinit var dao: ElectionDao
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var utils: DataUtils

    @Before
    fun setUp() {
        dao = mock(ElectionDao::class.java)
        firebaseDatabase = mock(FirebaseDatabase::class.java)
        utils = mock(DataUtils::class.java)
        repository = ElectionRepositoryImpl(firebaseDatabase, dao, utils)
    }

    @Test
    fun loadElectionsLocally() = runBlocking {
        val expected = getElections()

        `when`(dao.getElections()).thenReturn(expected.items.toElectionsWithResultsAndParty())

        assertEquals(expected, repository.getElectionsLocally())
    }

    @Test
    fun loadElectionsRemotely(): Unit = runBlocking {
        `when`(utils.isConnectedToInternet()).thenReturn(true)

        runCatching { repository.getElectionsRemotely() }.let { result ->
            result.onFailure { assertTrue(it is NullPointerException) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun `failure when getElectionsRemotely with no internet`(): Unit = runBlocking {
        `when`(utils.isConnectedToInternet()).thenReturn(false)

        repository.getElectionsRemotely().let { result ->
            result.onFailure { assertEquals(NO_INTERNET_CONNECTION, it.message) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun insertElections(): Unit = runBlocking {
        repository.saveElections(getElectionList())

        verify(dao).insertElections(getElectionList().toElectionsWithResultsAndParty())
    }
}
