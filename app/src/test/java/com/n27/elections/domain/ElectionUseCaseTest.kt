package com.n27.elections.domain

import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.domain.models.GeneralElections
import com.n27.elections.domain.repositories.ElectionRepository
import com.n27.test.generators.getElection
import com.n27.test.generators.getElectionList
import com.n27.test.generators.getGeneralElection
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class ElectionUseCaseTest {

    private lateinit var repository: ElectionRepository
    private lateinit var useCase: ElectionUseCase
    private val generalElections = GeneralElections(
        getElectionList(),
        listOf(getElection(chamberName = KEY_SENATE))
    )

    @Before
    fun setUp() {
        repository = mock(ElectionRepository::class.java)
        useCase = ElectionUseCase(repository)
    }

    @Test
    fun loadElectionsRemotely() = runTest {
        val expected = success(generalElections)

        `when`(repository.getElectionsRemotely()).thenReturn(success(getGeneralElection()))

        assertEquals(expected, useCase.getElections().first())
    }

    @Test
    fun `load elections locally when remotely fails`() = runTest {
        val expected = success(generalElections)

        `when`(repository.getElectionsRemotely()).thenReturn(failure(Throwable()))
        `when`(repository.getElectionsLocally()).thenReturn(getGeneralElection())

        assertEquals(expected, useCase.getElections().first())
    }

    @Test
    fun `failure when locally and remotely fail`() = runTest {
        val expected = NO_INTERNET_CONNECTION

        `when`(repository.getElectionsRemotely()).thenReturn(failure(Throwable(expected)))
        `when`(repository.getElectionsLocally()).thenReturn(null)

        useCase.getElections().first().onFailure { assertEquals(expected, it.message) }
    }
}