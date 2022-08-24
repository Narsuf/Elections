package com.jorgedguezm.elections.presentation.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.jorgedguezm.elections.data.DataUtils
import com.jorgedguezm.elections.data.ElectionApi
import com.jorgedguezm.elections.data.ElectionApiTest
import com.jorgedguezm.elections.data.ElectionRepository
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.data.models.ElectionGenerator.Companion.generateElection
import com.jorgedguezm.elections.data.room.ElectionDao
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class MainViewModelTests {

    private lateinit var electionRepository: ElectionRepository
    private lateinit var viewModel: MainViewModel
    private val expectedResponse = ElectionApiTest.expectedApiResponse

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = UnconfinedTestDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun init() = runTest {
        electionRepository = mock(ElectionRepository::class.java)
        electionRepository.utils = DataUtils(ApplicationProvider.getApplicationContext())
        electionRepository.dao = mock(ElectionDao::class.java)
        electionRepository.service = mock(ElectionApi::class.java)

        `when`(electionRepository.loadElections(anyString(), anyString()))
            .thenReturn(expectedResponse.data)

        viewModel = MainViewModel(electionRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadElections() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.loadElections(anyString(), anyString())

            assertEquals(MainViewState.Success(expectedResponse.data),
                viewModel.electionsResult.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadTaxisException() = runTest {
        val exception = IndexOutOfBoundsException()

        `when`(electionRepository.loadElections(anyString(), anyString())).thenThrow(exception)

        val totalExecutionTime = measureTimeMillis {
            viewModel.loadElections(anyString(), anyString())
            assertEquals(MainViewState.Error(exception), viewModel.electionsResult.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun sortElections() {
        val elections = mutableListOf<Election>()

        // Generate 100 elections to reduce error margin.
        for (i in 1..100) { elections.add(generateElection()) }

        val sortedElections = viewModel.sortElections(elections)
        var lastElection: Election? = null

        sortedElections.forEach {
            val currentDate = it.date.toInt()
            val lastDate = lastElection?.date?.toInt() ?: Int.MAX_VALUE
            assertTrue(currentDate <= lastDate)
            lastElection = it
        }
    }
}
