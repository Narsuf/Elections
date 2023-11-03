package com.n27.elections.domain

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.test.generators.getGeneralElections
import com.n27.test.generators.getGeneralElectionsRaw
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

    @Before
    fun setUp() {
        repository = mock(ElectionRepository::class.java)
        useCase = ElectionUseCase(repository)
    }

    @Test
    fun loadElectionsRemotely() = runTest {
        val expected = success(getGeneralElections())

        `when`(repository.getElectionsRemotely()).thenReturn(success(getGeneralElectionsRaw()))

        assertEquals(expected, useCase.getElections().first())
    }

    @Test
    fun loadElectionsLocallyWhenRemotelyFails() = runTest {
        val expected = success(getGeneralElections())

        `when`(repository.getElectionsRemotely()).thenReturn(failure(Throwable()))
        `when`(repository.getElectionsLocally()).thenReturn(getGeneralElectionsRaw())

        assertEquals(expected, useCase.getElections().first())
    }

    @Test
    fun failureWhenLocallyAndRemotelyFail() = runTest {
        val expected = NO_INTERNET_CONNECTION

        `when`(repository.getElectionsRemotely()).thenReturn(failure(Throwable(expected)))
        `when`(repository.getElectionsLocally()).thenReturn(null)

        useCase.getElections().first().onFailure { assertEquals(expected, it.message) }
    }
}