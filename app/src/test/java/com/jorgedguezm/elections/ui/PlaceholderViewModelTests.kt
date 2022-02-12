package com.jorgedguezm.elections.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider

import com.jorgedguezm.elections.api.ApiInterface
import com.jorgedguezm.elections.repository.ElectionRepository
import com.jorgedguezm.elections.retrofit.ApiInterfaceTests
import com.jorgedguezm.elections.room.ElectionDao
import com.jorgedguezm.elections.utils.Utils
import com.jorgedguezm.elections.view.ui.main.MainViewState
import com.jorgedguezm.elections.view.ui.main.PlaceholderViewModel

import junit.framework.TestCase.assertEquals

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

import org.mockito.Mockito.*

import org.robolectric.RobolectricTestRunner

import kotlin.system.measureTimeMillis

@RunWith(RobolectricTestRunner::class)
class PlaceholderViewModelTests {

    private lateinit var electionRepository: ElectionRepository
    private lateinit var viewModel: PlaceholderViewModel
    private lateinit var apiInterface: ApiInterface
    private val expectedResponse = ApiInterfaceTests.expectedApiResponse

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    private val testDispatcher = TestCoroutineDispatcher()

    @ExperimentalCoroutinesApi
    @Before
    fun init() = runBlockingTest {
        electionRepository = mock(ElectionRepository::class.java)
        electionRepository.utils = Utils(ApplicationProvider.getApplicationContext())
        electionRepository.dao = mock(ElectionDao::class.java)

        apiInterface = mock(ApiInterface::class.java)
        `when`(apiInterface.getElections(anyString(), anyString())).thenReturn(expectedResponse)

        electionRepository.service = apiInterface

        viewModel = PlaceholderViewModel(electionRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadElections() = runBlockingTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.loadElections(anyString(), anyString())

            assertEquals(MainViewState.Success(expectedResponse.elections),
                viewModel.electionsResult.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @ExperimentalCoroutinesApi
    @Test
    fun loadTaxisException() = runBlockingTest {
        val exception = IndexOutOfBoundsException()

        `when`(electionRepository.loadElections(anyString(), anyString())).thenThrow(exception)

        val totalExecutionTime = measureTimeMillis {
            viewModel.loadElections(anyString(), anyString())
            assertEquals(MainViewState.Error(exception), viewModel.electionsResult.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }
}