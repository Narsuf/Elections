package com.n27.core.presentation.detail

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.presentation.detail.mappers.toWithData
import com.n27.core.presentation.detail.models.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.models.DetailAction.ShowProgressBar
import com.n27.core.presentation.detail.models.DetailState.Content
import com.n27.core.presentation.detail.models.DetailState.Error
import com.n27.core.presentation.detail.models.DetailState.Loading
import com.n27.test.generators.getElection
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
import org.mockito.Mockito.anyString
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    private lateinit var repository: LiveRepository
    private lateinit var viewModel: DetailViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        repository = mock(LiveRepository::class.java)
        `when`(repository.getRegionalElection(anyString())).thenReturn(getElection())
        viewModel = DetailViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit loading`() = runTest {
        assertEquals(Loading, viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit content when election not null`() = runTest {
        viewModel.requestElection(getElection(), null, null)
        runCurrent()

        assertEquals(Content, viewModel.viewState.value)
        assertEquals(getElection().toWithData(), viewModel.viewContentState.value)
    }

    @Test
    fun `requestElection should emit content when electionId not null`() = runTest {
        viewModel.requestElection(null, "01", null)
        runCurrent()

        assertEquals(Content, viewModel.viewState.value)
        assertEquals(getElection().toWithData(), viewModel.viewContentState.value)
    }

    @Test
    fun `requestElection should emit content when electionIds not null`() = runTest {
        val ids = LocalElectionIds("01", "01", "01")
        `when`(repository.getLocalElection(ids)).thenReturn(getElection())
        viewModel.requestElection(null, null, ids)
        runCurrent()

        assertEquals(Content, viewModel.viewState.value)
        assertEquals(getElection().toWithData(), viewModel.viewContentState.value)
    }

    @Test
    fun `requestElection should emit error when all fields null`() = runTest {
        viewModel.requestElection(null, null, null)
        runCurrent()

        assertEquals(Error(), viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit error when exception occurs`() = runTest {
        `when`(repository.getRegionalElection(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))

        viewModel.requestElection(null, "01", null)
        runCurrent()

        assertEquals(Error(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }

    @Test
    fun `should ShowErrorSnackbar when exception occurs and lastState is content`() = runTest {
        viewModel.requestElection(null, "01", null)
        runCurrent()

        `when`(repository.getRegionalElection(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        viewModel.requestElection(null, "01", null)
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(NO_INTERNET_CONNECTION))
        observer.close()
        assertEquals(Content, viewModel.viewState.value)
        assertEquals(getElection().toWithData(), viewModel.viewContentState.value)
    }

    @Test
    fun `should ShowProgressBar and emit Content when requestElection is called and lastState was content`() = runTest {
        viewModel.requestElection(null, "01", null)
        runCurrent()

        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)
        viewModel.requestElection(null, "01", null)
        runCurrent()

        observer.assertValue(ShowProgressBar)
        observer.close()
        assertEquals(Content, viewModel.viewState.value)
        assertEquals(getElection().toWithData(), viewModel.viewContentState.value)
    }
}
