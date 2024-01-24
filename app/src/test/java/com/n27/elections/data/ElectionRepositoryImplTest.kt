package com.n27.elections.data

import com.n27.core.data.local.room.ElectionDao
import com.n27.core.data.local.room.mappers.toElectionsWithResultsAndParty
import com.n27.core.data.remote.firebase.FirebaseApi
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getElections
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryImplTest {

    private lateinit var repository: ElectionRepositoryImpl
    private lateinit var dao: ElectionDao
    private lateinit var api: FirebaseApi
    private val expectedError = "Parameter specified as non-null is null: method com.n27.core.data.remote.firebase." +
            "mappers.FirebaseMapperKt.toElections, parameter <this>"

    @Before
    fun setUp() {
        dao = mock(ElectionDao::class.java)
        api = mock(FirebaseApi::class.java)
        repository = ElectionRepositoryImpl(api, dao)
    }

    @Test
    fun loadElectionsLocally() = runBlocking {
        val expected = getElections()

        `when`(dao.getElections()).thenReturn(expected.items.toElectionsWithResultsAndParty())

        assertEquals(expected, repository.getElectionsLocally())
    }

    @Test
    fun loadElectionsRemotely(): Unit = runBlocking {
        `when`(api.get("elections")).thenReturn(flowOf(success(any())))

       repository.getElectionsRemotely().let { result ->
           result.onFailure { assertEquals(expectedError, it.message) }
           assertNull(result.getOrNull())
       }
    }

    @Test
    fun insertElections(): Unit = runBlocking {
        repository.saveElections(getElectionList())

        verify(dao).insertElections(getElectionList().toElectionsWithResultsAndParty())
    }
}
