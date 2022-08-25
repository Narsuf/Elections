package com.jorgedguezm.elections.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.room.ElectionDao
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.anyList
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ElectionRepositoryTests {

    private lateinit var repository: ElectionRepository
    private lateinit var service: ElectionApi
    private lateinit var dao: ElectionDao
    private lateinit var utils: DataUtils
    private val expectedResponse = ElectionApiTest.expectedApiResponse

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        service = mock(ElectionApi::class.java)
        dao = mock(ElectionDao::class.java)
        utils = mock(DataUtils::class.java)
        utils.context = ApplicationProvider.getApplicationContext()
        repository = ElectionRepository(service, dao, utils)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadElectionsFromDb() = runTest {
        val daoElections = expectedResponse.data

        `when`(utils.isConnectedToInternet()).thenReturn(false)
        `when`(dao.queryElections(anyString(), anyString())).thenReturn(daoElections)

        assertEquals(repository.loadElections("", ""), daoElections)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadElectionsFromApi() = runTest {
        val apiElections = expectedResponse

        `when`(utils.isConnectedToInternet()).thenReturn(true)
        `when`(service.getElections(anyString(), anyString())).thenReturn(apiElections)

        assertEquals(repository.loadElections("", ""), apiElections.data)
        verify(dao, times(1)).insertElections(anyList())
    }
}
