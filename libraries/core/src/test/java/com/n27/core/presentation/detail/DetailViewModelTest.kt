package com.n27.core.presentation.detail

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailFlags
import com.n27.core.presentation.detail.models.DetailInteraction.ScreenOpened
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import com.n27.test.generators.getElection
import com.n27.test.generators.getLiveElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class DetailViewModelTest {

    private lateinit var repository: LiveRepositoryImpl
    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        repository = mock(LiveRepositoryImpl::class.java)
        `when`(repository.getRegionalElection(anyString())).thenReturn(flowOf(success(getLiveElection())))
        viewModel = DetailViewModel(repository, null)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit content when election not null`() = runTest {
        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = getElection(),
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection().toContent(), viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit content when electionId not null`() = runTest {
        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = null,
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = "01",
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection().toContent(), viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit content when electionIds not null`() = runTest {
        val ids = LocalElectionIds("01", "01", "01")
        `when`(repository.getLocalElection(ids)).thenReturn(flowOf(success(getLiveElection())))

        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = null,
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = ids
                )
            )
        )
        runCurrent()

        assertEquals(getElection().toContent(), viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit error when all fields null`() = runTest {
        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = null,
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit error when exception occurs`() = runTest {
        `when`(repository.getRegionalElection(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))

        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = null,
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = "01",
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `should ShowErrorSnackbar and Content when exception occurs and lastState is content`() = runTest {
        val flags =  DetailFlags(
            election = null,
            isLiveGeneralElection = false,
            liveRegionalElectionId = "01",
            liveLocalElectionIds = null
        )

        viewModel.handleInteraction(ScreenOpened(flags))
        runCurrent()

        `when`(repository.getRegionalElection(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))
        viewModel.handleInteraction(ScreenOpened(flags))
        runCurrent()

        assertEquals(ShowErrorSnackbar(NO_INTERNET_CONNECTION), viewModel.viewAction.value)
    }
}
