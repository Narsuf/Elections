package com.n27.elections.presentation.main

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NOT_FIRST_LAUNCH
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.presentation.PresentationUtils
import com.n27.elections.ElectionsApplication
import com.n27.elections.data.ElectionRepository
import com.n27.elections.presentation.MainViewModel
import com.n27.elections.presentation.entities.MainEvent.*
import com.n27.elections.presentation.entities.MainInteraction.*
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Success
import com.n27.test.generators.getElection
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

    private lateinit var repository: ElectionRepository
    private lateinit var utils: PresentationUtils
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: SharedPreferences
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() = runTest {
        repository = mock(ElectionRepository::class.java)
        utils = mock(PresentationUtils::class.java)
        sharedPreferences = mock(SharedPreferences::class.java)

        `when`(repository.getElections()).thenReturn(getElections())

        viewModel = MainViewModel(repository, utils, sharedPreferences)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `screen opened should emit success when succeeding`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.handleInteraction(ScreenOpened)

            assertEquals(
                Success(getElections(), viewModel::onElectionClicked),
                viewModel.viewState.value
            )
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun `dialog dismissed should save on shared preferences`() = runTest {
        viewModel.sharedPreferences = ApplicationProvider.getApplicationContext<ElectionsApplication>()
            .getSharedPreferences("prefs", MODE_PRIVATE)

        viewModel.handleInteraction(DialogDismissed)

        assertTrue(viewModel.sharedPreferences.contains(NOT_FIRST_LAUNCH))
    }

    @Test
    fun `refresh should emit success when succeeding`() = runTest {
        viewModel.handleInteraction(Refresh)

        assertEquals(
            Success(getElections(), viewModel::onElectionClicked),
            viewModel.viewState.value
        )
    }

    @Test
    fun `screen opened should emit network error when empty elections and no connection`() = runTest {
        `when`(repository.getElections())
            .thenThrow(IndexOutOfBoundsException((NO_INTERNET_CONNECTION)))

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when empty elections`() = runTest {
        `when`(repository.getElections()).thenThrow(IndexOutOfBoundsException())

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when failing`() = runTest {
        val exception = IndexOutOfBoundsException()
        `when`(repository.getElections()).thenThrow(exception)

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `first launch should emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)

        viewModel.handleInteraction(ScreenOpened)

        observer.assertValues(ShowDisclaimer)
        observer.close()
    }

    @Test
    fun `not first launch should not emit show disclaimer`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        `when`(sharedPreferences.contains(NOT_FIRST_LAUNCH)).thenReturn(true)

        viewModel.handleInteraction(ScreenOpened)

        observer.assertValues()
        observer.close()
    }

    @Test
    fun `election clicked should emit navigate to detail event`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        val congressElection = getElection()
        val senateElection = getElection(KEY_SENATE)

        viewModel.onElectionClicked(congressElection, senateElection)

        observer.assertValues(NavigateToDetail(congressElection, senateElection))
        observer.close()
    }
}
