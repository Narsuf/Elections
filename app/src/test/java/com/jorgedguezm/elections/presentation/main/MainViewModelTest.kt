package com.jorgedguezm.elections.presentation.main

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.jorgedguezm.elections.data.ElectionApi
import com.jorgedguezm.elections.data.ElectionApiTest
import com.jorgedguezm.elections.data.ElectionRepository
import com.jorgedguezm.elections.data.room.ElectionDao
import com.jorgedguezm.elections.data.DataUtils
import com.jorgedguezm.elections.data.utils.ElectionGenerator.Companion.generateElection
import com.jorgedguezm.elections.presentation.common.Errors.NO_INTERNET_CONNECTION
import com.jorgedguezm.elections.presentation.main.entities.MainEvent.*
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.jorgedguezm.elections.presentation.main.entities.MainState.Error
import com.jorgedguezm.elections.presentation.main.entities.MainState.Success
import com.jorgedguezm.elections.utils.FlowTestObserver
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.channelFlow
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

    private lateinit var electionRepository: ElectionRepository
    private lateinit var analytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics
    private lateinit var viewModel: MainViewModel
    private lateinit var dataUtils: DataUtils
    private val expectedResponse = ElectionApiTest.expectedApiResponse
    private val flow = channelFlow { send(expectedResponse.elections) }
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun init() = runTest {
        electionRepository = mock(ElectionRepository::class.java)
        analytics = mock(FirebaseAnalytics::class.java)
        crashlytics = mock(FirebaseCrashlytics::class.java)
        dataUtils = mock(DataUtils::class.java)
        electionRepository.dataUtils = DataUtils(ApplicationProvider.getApplicationContext())
        electionRepository.dao = mock(ElectionDao::class.java)
        electionRepository.service = mock(ElectionApi::class.java)
        electionRepository.firebaseDatabase = mock(FirebaseDatabase::class.java)

        `when`(electionRepository.getElections()).thenReturn(flow)

        viewModel = MainViewModel(electionRepository, analytics, crashlytics, dataUtils)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `screen opened should emit success when succeeding`() = runTest {
        val totalExecutionTime = measureTimeMillis {
            viewModel.handleInteraction(ScreenOpened)

            assertEquals(Success(expectedResponse.elections, viewModel::onElectionClicked), viewModel.viewState.value)
        }

        println("Total Execution Time: $totalExecutionTime ms")
    }

    @Test
    fun `refresh should emit success when succeeding`() = runTest {
        viewModel.handleInteraction(MainInteraction.Refresh)

        assertEquals(Success(expectedResponse.elections, viewModel::onElectionClicked), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit network error when empty elections and no connection`() = runTest {
        `when`(electionRepository.getElections()).thenReturn(channelFlow { send(listOf()) })

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when empty elections and connection`() = runTest {
        `when`(electionRepository.getElections()).thenReturn(channelFlow { send(listOf()) })
        `when`(dataUtils.isConnectedToInternet()).thenReturn(true)

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when failing`() = runTest {
        val exception = IndexOutOfBoundsException()
        `when`(electionRepository.getElections()).thenThrow(exception)

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit success when failing with fallback`() = runTest {
        val exception = IndexOutOfBoundsException("Failed to connect to ")
        `when`(electionRepository.getElections()).thenThrow(exception)
        `when`(electionRepository.getElections(fallback = true)).thenReturn(flow)

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Success(expectedResponse.elections, viewModel::onElectionClicked), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when both main server and fallback fail`() = runTest {
        val exception = IndexOutOfBoundsException("Failed to connect to ")
        `when`(electionRepository.getElections()).thenThrow(exception)
        `when`(electionRepository.getElections(fallback = true)).thenThrow(exception)

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `election clicked should emit navigate to detail event`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        val congressElection = generateElection()
        val senateElection = generateElection()

        viewModel.onElectionClicked(congressElection, senateElection)

        observer.assertValues(NavigateToDetail(congressElection, senateElection))
        observer.close()
    }
}
