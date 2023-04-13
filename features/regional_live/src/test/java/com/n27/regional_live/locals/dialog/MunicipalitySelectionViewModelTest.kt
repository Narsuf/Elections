package com.n27.regional_live.locals.dialog

import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.LiveRepository
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.regional_live.locals.comm.LocalsEvent
import com.n27.regional_live.locals.comm.LocalsEvent.RequestElection
import com.n27.regional_live.locals.comm.LocalsEvent.ShowError
import com.n27.regional_live.locals.comm.LocalsEventBus
import com.n27.regional_live.locals.dialog.models.MunicipalityAction.PopulateMunicipalitiesSpinner
import com.n27.regional_live.locals.dialog.models.MunicipalityAction.ShowErrorSnackbar
import com.n27.regional_live.locals.dialog.models.MunicipalityState.Content
import com.n27.regional_live.locals.dialog.models.MunicipalityState.Empty
import com.n27.test.generators.getMunicipalities
import com.n27.test.generators.getProvince
import com.n27.test.generators.getProvinces
import com.n27.test.generators.getRegion
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
class MunicipalitySelectionViewModelTest {

    private lateinit var repository: LiveRepository
    private lateinit var eventBus: LocalsEventBus
    private lateinit var viewModel: MunicipalitySelectionViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun init() = runTest {
        repository = mock(LiveRepository::class.java)
        eventBus = LocalsEventBus()

        `when`(repository.getProvinces(anyString())).thenReturn(getProvinces())
        `when`(repository.getMunicipalities(anyString())).thenReturn(getMunicipalities())

        Dispatchers.setMain(testDispatcher)

        viewModel = MunicipalitySelectionViewModel(repository, eventBus)
    }

    @Test
    fun `view model initialized should emit Empty`() = runTest {
        assertEquals(Empty, viewModel.viewState.value)
    }

    @Test
    fun `requestProvinces should emit Content`() = runTest {
        val expected = Content(getProvinces())

        viewModel.requestProvinces(getRegion())
        runCurrent()

        assertEquals(expected, viewModel.viewState.value)
    }

    @Test
    fun `requestProvinces should emit ShowErrorSnackbar when region is null`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestProvinces(null)
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
    }

    @Test
    fun `requestProvinces should emit ShowErrorSnackbar when exception occurs`() = runTest {
        `when`(repository.getProvinces(anyString())).thenThrow(IndexOutOfBoundsException())
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestProvinces(getRegion())
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
    }

    @Test
    fun `requestMunicipalities should emit PopulateMunicipalitiesSpinner`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestMunicipalities(getProvince())
        runCurrent()

        observer.assertValue(PopulateMunicipalitiesSpinner(getMunicipalities()))
        observer.close()
    }

    @Test
    fun `requestMunicipalities should emit ShowErrorSnackbar when region is null`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestMunicipalities(null)
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(null))
        observer.close()
    }

    @Test
    fun `requestMunicipalities should emit ShowErrorSnackbar when exception occurs`() = runTest {
        `when`(repository.getMunicipalities(anyString())).thenThrow(IndexOutOfBoundsException(NO_INTERNET_CONNECTION))
        val observer = FlowTestObserver(this + testDispatcher, viewModel.viewAction)

        viewModel.requestMunicipalities(getProvince())
        runCurrent()

        observer.assertValue(ShowErrorSnackbar(NO_INTERNET_CONNECTION))
        observer.close()
    }

    @Test
    fun `requestElection should emit RequestElection`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, eventBus.event)
        val regionId = "01"
        val provinceId = "02"
        val municipalityId = "10"

        viewModel.requestElection(regionId, provinceId, municipalityId)
        runCurrent()

        observer.assertValue(RequestElection(LocalElectionIds(regionId, provinceId, municipalityId)))
        observer.close()
    }

    @Test
    fun `requestElection should emit ShowError when one param is null`() = runTest {
        val observer = FlowTestObserver(this + testDispatcher, eventBus.event)

        viewModel.requestElection(null, "", "")
        runCurrent()

        observer.assertValue(ShowError)
        observer.close()
    }
}
