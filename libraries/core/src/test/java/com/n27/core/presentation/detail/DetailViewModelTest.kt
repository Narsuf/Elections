package com.n27.core.presentation.detail

import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.presentation.detail.mappers.toContent
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailFlags
import com.n27.core.presentation.detail.models.DetailInteraction.Refresh
import com.n27.core.presentation.detail.models.DetailInteraction.ScreenOpened
import com.n27.core.presentation.detail.models.DetailInteraction.Swap
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
import kotlin.Result.Companion.failure
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
        `when`(repository.getCongressElection()).thenReturn(flowOf(success(getLiveElection())))
        `when`(repository.getRegionalElection(anyString())).thenReturn(flowOf(success(getLiveElection())))
        `when`(repository.getSenateElection()).thenReturn(
            flowOf(success(getLiveElection(getElection(chamberName = KEY_SENATE))))
        )

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
    fun `ScreenOpened should emit content when liveGeneralElection`() = runTest {
        viewModel.handleInteraction(
            ScreenOpened(
                DetailFlags(
                    election = null,
                    isLiveGeneralElection = true,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection().toContent(), viewModel.viewState.value)
    }

    @Test
    fun `ScreenOpened should emit content when regionElectionId not null`() = runTest {
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
    fun `ScreenOpened should emit content when localElectionIds not null`() = runTest {
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
    fun `ScreenOpened should emit error onFailure`() = runTest {
        `when`(repository.getRegionalElection(anyString()))
            .thenReturn(flowOf(failure(Throwable(NO_INTERNET_CONNECTION))))

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
    fun `onFailure should ShowErrorSnackbar and Content when lastState is content`() = runTest {
        val flags =  DetailFlags(
            election = null,
            isLiveGeneralElection = false,
            liveRegionalElectionId = "01",
            liveLocalElectionIds = null
        )

        viewModel.handleInteraction(ScreenOpened(flags))
        runCurrent()

        `when`(repository.getRegionalElection(anyString()))
            .thenReturn(flowOf(failure(Throwable(NO_INTERNET_CONNECTION))))
        viewModel.handleInteraction(ScreenOpened(flags))
        runCurrent()

        assertEquals(ShowErrorSnackbar(NO_INTERNET_CONNECTION), viewModel.viewAction.value)
    }

    @Test
    fun `Refresh should emit Content`() = runTest {
        viewModel.handleInteraction(
            Refresh(
                getElection(),
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
    fun `Refresh should emit Content with live senate when current election is live senate`() = runTest {
        viewModel.handleInteraction(
            Refresh(
                getElection(chamberName = KEY_SENATE, validVotes = 1),
                DetailFlags(
                    election = getElection(),
                    isLiveGeneralElection = true,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection(chamberName = KEY_SENATE).toContent(), viewModel.viewState.value)
    }

    @Test
    fun `Swipe should emit Content with congress when current chamber is senate`() = runTest {
        viewModel.handleInteraction(
            Swap(
                getElection(chamberName = KEY_SENATE),
                getElection(chamberName = KEY_SENATE),
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
    fun `Swipe should emit Content with senate when current chamber is congress`() = runTest {
        viewModel.handleInteraction(
            Swap(
                getElection(chamberName = KEY_SENATE),
                getElection(),
                DetailFlags(
                    election = getElection(),
                    isLiveGeneralElection = false,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection(chamberName = KEY_SENATE).toContent(), viewModel.viewState.value)
    }

    @Test
    fun `Swipe should emit Content with live senate when current chamber is live congress`() = runTest {
        viewModel.handleInteraction(
            Swap(
                null,
                getElection(),
                DetailFlags(
                    election = getElection(),
                    isLiveGeneralElection = true,
                    liveRegionalElectionId = null,
                    liveLocalElectionIds = null
                )
            )
        )
        runCurrent()

        assertEquals(getElection(chamberName = KEY_SENATE).toContent(), viewModel.viewState.value)
    }
}
