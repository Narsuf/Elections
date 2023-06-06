package com.n27.regional_live.presentation.regionals

import com.n27.core.data.LiveRepositoryImpl
import com.n27.regional_live.presentation.regionals.models.RegionalsAction.ShowErrorSnackbar
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Content
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Error
import com.n27.regional_live.presentation.regionals.models.RegionalsState.Loading
import com.n27.test.generators.getLiveElections
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
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import kotlin.Result.Companion.success

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
class RegionalsViewModelTest {

    private lateinit var repository: LiveRepositoryImpl
    private lateinit var viewModel: RegionalsViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        repository = mock(LiveRepositoryImpl::class.java)
        `when`(repository.getRegionalElections()).thenReturn(flowOf(success(getLiveElections())))
        viewModel = RegionalsViewModel(repository, null)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `requestElections should emit content when elections not empty`() = runTest {
        val expected = Content(getLiveElections())

        viewModel.requestElections()
        runCurrent()

        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `requestElections should emit error when elections is empty`() = runTest {
        `when`(repository.getRegionalElections()).thenThrow(IndexOutOfBoundsException())
        val expected = Error()

        viewModel.requestElections()
        runCurrent()

        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `exception should emit ShowErrorSnackbar when lastState was Content`() = runTest {
        viewModel.requestElections()
        runCurrent()

        `when`(repository.getRegionalElections()).thenThrow(IndexOutOfBoundsException())
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        val expected = Content(getLiveElections())
        viewModel.requestElections()
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
        assertEquals(expected, viewModel.viewState.value)
    }
}
