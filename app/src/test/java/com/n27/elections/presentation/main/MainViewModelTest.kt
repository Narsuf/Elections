package com.n27.elections.presentation.main

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.ElectionsApplication
import com.n27.elections.data.AppRepository
import com.n27.elections.data.ElectionRepository
import com.n27.elections.presentation.MainViewModel
import com.n27.elections.presentation.entities.MainEvent.*
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Success
import com.n27.test.generators.getElections
import com.n27.test.observers.FlowTestObserver
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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
    private lateinit var sharedPreferences: SharedPreferences
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() = runTest {
        appRepository = mock(AppRepository::class.java)
        electionRepository = mock(ElectionRepository::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)

        `when`(appRepository.isFirstLaunch()).thenReturn(false)
        `when`(electionRepository.getElections()).thenReturn(getElections())

        viewModel = MainViewModel(appRepository, electionRepository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `screen opened should emit success when succeeding`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.requestElections(initialLoading = true)

            assertEquals(
                Success(getElections()),
                viewModel.viewState.value
            )
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    /*@Test
    fun `dialog dismissed should save on shared preferences`() = runTest {
        viewModel.sharedPreferences = ApplicationProvider.getApplicationContext<ElectionsApplication>()
            .getSharedPreferences("prefs", MODE_PRIVATE)

        viewModel.saveFirstLaunchFlag()

        assertTrue(viewModel.sharedPreferences.contains(NOT_FIRST_LAUNCH))
    }*/

    @Test
    fun `refresh should emit success when succeeding`() = runTest {
        viewModel.requestElections()

        assertEquals(
            Success(getElections()),
            viewModel.viewState.value
        )
    }

    @Test
    fun `screen opened should emit network error when empty elections and no connection`() = runTest {
        `when`(electionRepository.getElections())
            .thenThrow(IndexOutOfBoundsException((NO_INTERNET_CONNECTION)))

        viewModel.requestElections(initialLoading = true)

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when empty elections`() = runTest {
        `when`(electionRepository.getElections()).thenThrow(IndexOutOfBoundsException())

        viewModel.requestElections(initialLoading = true)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when failing`() = runTest {
        val exception = IndexOutOfBoundsException()
        `when`(electionRepository.getElections()).thenThrow(exception)

        viewModel.requestElections(initialLoading = true)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `first launch should emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        `when`(appRepository.isFirstLaunch()).thenReturn(true)

        viewModel.requestElections(initialLoading = true)

        observer.assertValues(ShowDisclaimer)
        observer.close()
    }

    @Test
    fun `not first launch should not emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)

        viewModel.requestElections(initialLoading = true)

        observer.assertValues()
        observer.close()
    }
}
