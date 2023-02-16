package com.narsuf.elections.presentation.main

import androidx.test.core.app.ApplicationProvider
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.database.FirebaseDatabase
import com.narsuf.elections.data.DataUtils
import com.narsuf.elections.data.ElectionApi
import com.narsuf.elections.data.ElectionRepository
import com.narsuf.elections.data.room.ElectionDao
import com.narsuf.elections.data.utils.getElection
import com.narsuf.elections.data.utils.getElections
import com.narsuf.elections.presentation.common.Constants.KEY_SENATE
import com.narsuf.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import com.narsuf.elections.presentation.main.entities.MainEvent.*
import com.narsuf.elections.presentation.main.entities.MainInteraction
import com.narsuf.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.narsuf.elections.presentation.main.entities.MainState.Error
import com.narsuf.elections.presentation.main.entities.MainState.Success
import com.narsuf.elections.utils.FlowTestObserver
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
    private val flow = channelFlow { send(Result.success(getElections())) }
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

        viewModel = MainViewModel(electionRepository, analytics, crashlytics)
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
    fun `refresh should emit success when succeeding`() = runTest {
        viewModel.handleInteraction(MainInteraction.Refresh)

        assertEquals(
            Success(getElections(), viewModel::onElectionClicked),
            viewModel.viewState.value
        )
    }

    @Test
    fun `screen opened should emit network error when empty elections and no connection`() = runTest {
        `when`(electionRepository.getElections())
            .thenReturn(channelFlow { send(Result.failure(Throwable(NO_INTERNET_CONNECTION))) })

        viewModel.handleInteraction(ScreenOpened)

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `screen opened should emit error when empty elections and connection`() = runTest {
        `when`(electionRepository.getElections()).thenReturn(channelFlow { send(Result.failure(Throwable())) })
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
    fun `election clicked should emit navigate to detail event`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewEvent)
        val congressElection = getElection()
        val senateElection = getElection(KEY_SENATE)

        viewModel.onElectionClicked(congressElection, senateElection)

        observer.assertValues(NavigateToDetail(congressElection, senateElection))
        observer.close()
    }
}