package com.n27.elections.presentation

import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.data.AppRepositoryImpl
import com.n27.elections.domain.ElectionUseCase
import com.n27.elections.domain.models.GeneralElections
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList
import com.n27.test.observers.FlowTestObserver
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success
import kotlin.system.measureTimeMillis

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    private lateinit var appRepository: AppRepositoryImpl
    private lateinit var useCase: ElectionUseCase
    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val generalElections = GeneralElections(
        getElectionList(),
        listOf(getElection(chamberName = KEY_SENATE))
    )

    @Before
    fun init() = runTest {
        appRepository = mock(AppRepositoryImpl::class.java)
        useCase = mock(ElectionUseCase::class.java)

        `when`(appRepository.isFirstLaunch()).thenReturn(false)
        `when`(useCase.getElections()).thenReturn(flowOf(success(generalElections)))

        viewModel = MainViewModel(appRepository, useCase)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit HasElections`() = runTest {
        assertEquals(getHasElections(), viewModel.uiState.value)
    }

    @Test
    fun `requestElections should emit content HasElections`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.requestElections()
            runCurrent()

            assertEquals(getHasElections(), viewModel.uiState.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun `requestElections should emit HasElections with error`() = runTest {
        `when`(useCase.getElections()).thenReturn(flowOf(failure(Throwable((NO_INTERNET_CONNECTION)))))

        viewModel.requestElections()
        runCurrent()

        assertEquals(getHasElections(error = NO_INTERNET_CONNECTION), viewModel.uiState.value)
    }

    @Test
    fun `first launch should emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        `when`(appRepository.isFirstLaunch()).thenReturn(true)

        viewModel.requestElections()
        runCurrent()

        observer.assertValues(ShowDisclaimer)
        observer.close()
    }

    @Test
    fun `not first launch should not emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestElections()
        runCurrent()

        observer.assertValues()
        observer.close()
    }
}
