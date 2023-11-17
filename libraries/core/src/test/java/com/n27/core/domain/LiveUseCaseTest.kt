package com.n27.core.domain

import com.n27.core.domain.live.LiveRepository
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.domain.region.RegionRepository
import com.n27.test.generators.getLiveElection
import com.n27.test.generators.getLiveElections
import com.n27.test.generators.getMunicipality
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegions
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import kotlin.Result.Companion.failure
import kotlin.Result.Companion.success

class LiveUseCaseTest {

    private lateinit var liveRepository: LiveRepository
    private lateinit var regionRepository: RegionRepository
    private lateinit var useCase: LiveUseCase
    private val ids = LocalElectionIds("", "", "")

    @Before
    fun setUp() = runBlocking {
        liveRepository = mock(LiveRepository::class.java)
        regionRepository = mock(RegionRepository::class.java)

        `when`(regionRepository.getRegions()).thenReturn(success(getRegions()))

        useCase = LiveUseCase(liveRepository, regionRepository)
    }

    @Test
    fun getCongressElection() = runTest {
        val expected = flowOf(success(getLiveElection()))

        `when`(liveRepository.getCongressElection()).thenReturn(expected)

        assertEquals(expected, useCase.getCongressElection())
    }

    @Test
    fun getSenateElection() = runTest {
        val expected = flowOf(success(getLiveElection()))

        `when`(liveRepository.getSenateElection()).thenReturn(expected)

        assertEquals(expected, useCase.getSenateElection())
    }

    @Test
    fun getRegionalElection() = runTest {
        val expected = flowOf(success(getLiveElection()))

        `when`(liveRepository.getRegionalElection("01", getRegions())).thenReturn(expected)

        assertEquals(expected.first(), useCase.getRegionalElection("01").first())
    }

    @Test
    fun getRegionalElectionFailure() = runTest {
        val expected = "Error"

        `when`(regionRepository.getRegions()).thenReturn(failure(Throwable(expected)))

        useCase.getRegionalElection("01").first().let { result ->
            result.onFailure { assertEquals(it.message, expected) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun getRegionalElections() = runTest {
        val expected = success(getLiveElections())

        `when`(liveRepository.getRegionalElections(getRegions())).thenReturn(expected)

        assertEquals(expected, useCase.getRegionalElections().first())
    }

    @Test
    fun getRegionalElectionsFailure() = runTest {
        val expected = "Error"

        `when`(regionRepository.getRegions()).thenReturn(failure(Throwable(expected)))

        useCase.getRegionalElections().first().let { result ->
            result.onFailure { assertEquals(it.message, expected) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun getLocalElection() = runTest {
        val expected = flowOf(success(getLiveElection()))

        `when`(regionRepository.getMunicipalityName(getRegions(), ids)).thenReturn("C")
        `when`(liveRepository.getLocalElection(ids, "C")).thenReturn(expected)

        assertEquals(expected.first(), useCase.getLocalElection(ids).first())
    }

    @Test
    fun getLocalElectionFailure() = runTest {
        val expected = "Error"

        `when`(regionRepository.getRegions()).thenReturn(failure(Throwable(expected)))

        useCase.getLocalElection(ids).first().let { result ->
            result.onFailure { assertEquals(it.message, expected) }
            assertNull(result.getOrNull())
        }
    }

    @Test
    fun getRegionsTest() = runTest {
        val expected = success(getRegions())

        assertEquals(expected, useCase.getRegions())
    }

    @Test
    fun getProvincesTest() = runTest {
        val expected = getProvinces()

        `when`(regionRepository.getProvinces("01")).thenReturn(expected)

        assertEquals(expected, useCase.getProvinces("01"))
    }

    @Test
    fun getMunicipalitiesSorted() = runTest {
        val expected = listOf(
            getMunicipality(),
            getMunicipality(name = "B"),
        )

        val actual = listOf(
            getMunicipality(name = "B"),
            getMunicipality()
        )

        `when`(regionRepository.getMunicipalities("01")).thenReturn(actual)

        assertEquals(expected, useCase.getMunicipalities("01"))
    }
}