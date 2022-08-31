package com.jorgedguezm.elections.data

import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.data.utils.DataUtils
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryTest {

    private lateinit var repository: ElectionRepository
    private lateinit var service: ElectionApi
    private lateinit var dao: ElectionDao
    private lateinit var utils: DataUtils
    private val expectedResponse = ElectionApiTest.expectedApiResponse
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        service = mock(ElectionApi::class.java)
        dao = mock(ElectionDao::class.java)
        utils = mock(DataUtils::class.java)
        utils.context = ApplicationProvider.getApplicationContext()
        repository = ElectionRepository(service, dao, utils)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun loadElectionsFromDb() = runTest {
        val daoElections = expectedResponse.data

        `when`(utils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.queryElections(anyString())).thenReturn(daoElections)

        assertEquals(repository.getElections(""), daoElections)
    }

    @Test
    fun `no internet connection and empty database should throw exception`() = runTest {
        val daoElections = listOf<Election>()

        `when`(utils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.queryElections(anyString())).thenReturn(daoElections)

        try {
           repository.getElections("")
        } catch (e: Exception) {
            assertEquals(e.message, "1")
        }
    }

    @Test
    fun loadElectionsFromApi() = runTest {
        val apiElections = expectedResponse

        `when`(utils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections(anyString())).thenReturn(apiElections)

        assertEquals(repository.getElections(""), apiElections.data)
        verify(dao, times(1)).insertElections(anyList())
    }
}
