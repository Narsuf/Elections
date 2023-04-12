package com.n27.elections.presentation

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.data.repositories.AppRepository
import com.n27.elections.data.repositories.ElectionRepository
import com.n27.elections.presentation.models.MainAction.*
import com.n27.elections.presentation.models.MainContentState.WithData
import com.n27.elections.presentation.models.MainState.*
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
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `requestElections should emit content when success`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.requestElections()
            runCurrent()

            assertEquals(WithData(getElections()), viewModel.viewContentState.value)
            assertEquals(Content, viewModel.viewState.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun `refresh should emit content when success`() = runTest {
        viewModel.requestElections()
        runCurrent()

        assertEquals(WithData(getElections()), viewModel.viewContentState.value)
        assertEquals(Content, viewModel.viewState.value)
    }

    @Test
    fun `requestElections should emit error when an exception occurs`() = runTest {
        `when`(electionRepository.getElections()).thenThrow(IndexOutOfBoundsException((NO_INTERNET_CONNECTION)))

        viewModel.requestElections()
        runCurrent()

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
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

    @Test
    fun `should ShowErrorSnackbar and Content when exception occurs and lastState is content`() = runTest {
        viewModel.requestElections()
        runCurrent()

        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        `when`(electionRepository.getElections()).thenThrow(IndexOutOfBoundsException((NO_INTERNET_CONNECTION)))
        viewModel.requestElections()
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(NO_INTERNET_CONNECTION))
        observer.close()
        assertEquals(WithData(getElections()), viewModel.viewContentState.value)
        assertEquals(Content, viewModel.viewState.value)
    }
}
