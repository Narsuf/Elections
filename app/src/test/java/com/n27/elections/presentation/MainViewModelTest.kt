package com.n27.elections.presentation

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.data.repositories.AppRepository
import com.n27.elections.data.repositories.ElectionRepository
import com.n27.elections.presentation.entities.MainEvent.*
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.InitialLoading
import com.n27.elections.presentation.entities.MainState.Success
import com.n27.test.generators.getElections
import com.n27.test.observers.FlowTestObserver
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    private lateinit var appRepository: AppRepository
    private lateinit var electionRepository: ElectionRepository
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        appRepository = mock(AppRepository::class.java)
        electionRepository = mock(ElectionRepository::class.java)

        `when`(appRepository.isFirstLaunch()).thenReturn(false)
        `when`(electionRepository.getElections()).thenReturn(getElections())

        viewModel = MainViewModel(appRepository, electionRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit initial loading`() = runTest {
        viewModel.requestElections()

        assertEquals(InitialLoading, viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit success when succeeding`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.requestElections(initialLoading = true)
            runCurrent()

            assertEquals(Success(getElections()), viewModel.viewState.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun `refresh should emit success when succeeding`() = runTest {
        viewModel.requestElections()
        runCurrent()

        assertEquals(Success(getElections()), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when an exception occurs`() = runTest {
        `when`(electionRepository.getElections()).thenThrow(IndexOutOfBoundsException((NO_INTERNET_CONNECTION)))

        viewModel.requestElections()
        runCurrent()

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `first launch should emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        `when`(appRepository.isFirstLaunch()).thenReturn(true)

        viewModel.requestElections()
        runCurrent()

        observer.assertValues(ShowDisclaimer)
        observer.close()
    }

    @Test
    fun `not first launch should not emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)

        viewModel.requestElections()
        runCurrent()

        observer.assertValues()
        observer.close()
    }
}
