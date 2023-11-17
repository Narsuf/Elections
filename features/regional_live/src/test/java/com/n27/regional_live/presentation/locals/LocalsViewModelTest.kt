package com.n27.regional_live.presentation.locals

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.domain.LiveUseCase
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.presentation.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.presentation.locals.comm.LocalsEventBus
import com.n27.regional_live.presentation.locals.entities.LocalsAction.NavigateToDetail
import com.n27.regional_live.presentation.locals.entities.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.locals.entities.LocalsState.Content
import com.n27.regional_live.presentation.locals.entities.LocalsState.Error
import com.n27.regional_live.presentation.locals.entities.LocalsState.Loading
import com.n27.test.generators.getLiveElection
import com.n27.test.generators.getRegions
import com.n27.test.observers.FlowTestObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
class LocalsViewModelTest {

    private lateinit var useCase: LiveUseCase
    private lateinit var eventBus: LocalsEventBus
    private lateinit var viewModel: LocalsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        useCase = mock(LiveUseCase::class.java)
        eventBus = LocalsEventBus()
        `when`(useCase.getRegions()).thenReturn(success(getRegions()))
        Dispatchers.setMain(testDispatcher)
        viewModel = LocalsViewModel(useCase, null, eventBus)
    }

    @Test
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `requestRegions should emit content onSuccess`() = runTest {
        val expected = Content(getRegions().regions)

        viewModel.requestRegions()
        runCurrent()

        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `requestRegions should emit error onFailure`() = runTest {
        `when`(useCase.getRegions()).thenReturn(failure(Throwable()))
        viewModel.requestRegions()
        runCurrent()

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `RequestElection should emit NavigateToDetail onSuccess`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        val ids = LocalElectionIds("", "", "")
        `when`(useCase.getLocalElection(ids)).thenReturn(flowOf(success(getLiveElection())))

        runCurrent()
        eventBus.emit(RequestElection(ids))
        runCurrent()

        observer.assertValue(NavigateToDetail(ids))
        observer.close()
    }

    @Test
    fun `RequestElection should emit ShowErrorSnackbar onFailure`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        val ids = LocalElectionIds("", "", "")
        `when`(useCase.getLocalElection(ids)).thenReturn(flowOf(failure(Throwable(NO_INTERNET_CONNECTION))))

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
        eventBus.emit(ShowError(Throwable()))
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
    }
}
