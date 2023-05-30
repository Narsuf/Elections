package com.n27.regional_live.presentation.locals

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepositoryImpl
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.regional_live.data.RegionRepositoryImpl
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.models.LocalsAction.NavigateToDetail
import com.n27.regional_live.presentation.locals.models.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.models.LocalsState.Content
import com.n27.regional_live.presentation.locals.models.LocalsState.Error
import com.n27.regional_live.presentation.locals.models.LocalsState.Loading
import com.n27.test.generators.getLiveElection
import com.n27.test.generators.getRegions
import com.n27.test.observers.FlowTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
class LocalsViewModelTest {

    private lateinit var regionRepository: RegionRepositoryImpl
    private lateinit var liveRepository: LiveRepositoryImpl
    private lateinit var eventBus: LocalsEventBus
    private lateinit var viewModel: LocalsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        regionRepository = mock(RegionRepositoryImpl::class.java)
        liveRepository = mock(LiveRepositoryImpl::class.java)
        eventBus = LocalsEventBus()
        `when`(regionRepository.getRegions()).thenReturn(success(getRegions()))
        Dispatchers.setMain(testDispatcher)
        viewModel = LocalsViewModel(liveRepository, regionRepository, null, eventBus)
    }

    @Test
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `requestRegions should emit content`() = runTest {
        val expected = Content(getRegions().regions)

        viewModel.requestRegions()
        runCurrent()

        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `requestRegions should emit error`() = runTest {
        `when`(regionRepository.getRegions()).thenThrow(IndexOutOfBoundsException())
        viewModel.requestRegions()
        runCurrent()

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `RequestElection should emit NavigateToDetail`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        val ids = LocalElectionIds("", "", "")
        `when`(liveRepository.getLocalElection(ids)).thenReturn(success(getLiveElection()))

        runCurrent()
        eventBus.emit(RequestElection(ids))
        runCurrent()

        observer.assertValue(NavigateToDetail(ids))
        observer.close()
    }

    @Test
    fun `RequestElection should emit ShowErrorSnackbar`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        val ids = LocalElectionIds("", "", "")
        `when`(liveRepository.getLocalElection(ids)).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))

        runCurrent()
        eventBus.emit(RequestElection(ids))
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(NO_INTERNET_CONNECTION))
        observer.close()
    }

    @Test
    fun `ShowError should emit ShowErrorSnackbar`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        runCurrent()
        eventBus.emit(ShowError)
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
    }
}
