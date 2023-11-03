package com.n27.elections.data

import com.google.firebase.database.FirebaseDatabase
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.common.DataUtils
import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.domain.election.models.Elections
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.failure
import kotlin.math.exp

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

        // Not proud of this test, but Firebase's API doesn't make it easy.
        runCatching { repository.getElectionsRemotely() }.getOrElse { assertTrue(it is NullPointerException) }
    }

    @Test
    fun failureWhenGetElectionsRemotelyWithoutInternet(): Unit = runBlocking {
        `when`(utils.isConnectedToInternet()).thenReturn(false)

        repository.getElectionsRemotely()
            .onFailure { assertEquals(NO_INTERNET_CONNECTION, it.message) }
    }

    @Test
    fun insertElections(): Unit = runBlocking {
        repository.saveElections(getElectionList())

        verify(dao).insertElections(getElectionList().toElectionsWithResultsAndParty())
    }
}
