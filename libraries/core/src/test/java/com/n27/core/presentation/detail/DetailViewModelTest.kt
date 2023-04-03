package com.n27.core.presentation.detail

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.InitialLoading
import com.n27.core.presentation.detail.DetailState.Success
import com.n27.test.generators.getElection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        viewModel = DetailViewModel(repository)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `view model initialized should emit initial loading`() = runTest {
        assertEquals(InitialLoading, viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit success when election not null`() = runTest {
        viewModel.requestElection(getElection(), null, null)
        runCurrent()

        assertEquals(Success(getElection()), viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit success when electionId not null`() = runTest {
        `when`(repository.getRegionalElection(anyString())).thenReturn(getElection())

        viewModel.requestElection(null, "01", null)
        runCurrent()

        assertEquals(Success(getElection()), viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit success when electionIds not null`() = runTest {
        val ids = LocalElectionIds("01", "01", "01")
        `when`(repository.getLocalElection(ids)).thenReturn(getElection())

        viewModel.requestElection(null, null, ids)
        runCurrent()

        assertEquals(Success(getElection()), viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit failure when all fields null`() = runTest {
        viewModel.requestElection(null, null, null)
        runCurrent()

        assertEquals(Failure(), viewModel.viewState.value)
    }

    @Test
    fun `requestElection should emit failure when exception occurs`() = runTest {
        `when`(repository.getRegionalElection(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))

        viewModel.requestElection(null, "01", null)
        runCurrent()

        assertEquals(Failure(NO_INTERNET_CONNECTION), viewModel.viewState.value)
    }
}
