package com.n27.regional_live.ui.regional_live.locals.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.data.json.models.Municipality
import com.n27.core.data.json.models.Province
import com.n27.core.data.models.Election
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.regional_live.R
import com.n27.regional_live.databinding.DialogMunicipalitySelectionBinding
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import com.n27.regional_live.ui.regional_live.locals.LocalsState
import com.n27.regional_live.ui.regional_live.locals.LocalsViewModel
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Failure
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Loading
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.ProvincesReceived
import com.n27.regional_live.ui.regional_live.locals.dialog.MunicipalityState.Success
import java.text.NumberFormat.getIntegerInstance
import javax.inject.Inject

class MunicipalitySelectionDialog : DialogFragment() {

    private var _binding: DialogMunicipalitySelectionBinding? = null
    private val binding get() = _binding!!
    @Inject internal lateinit var viewModel: MunicipalitySelectionViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogMunicipalitySelectionBinding.inflate(layoutInflater)

        binding.setUpViews()
        initObservers()

        /*val provinces = arguments?.getSerializable(KE) as Election

        val textViewTitle = inflatedLayout.findViewById(R.id.text_view_title) as TextView
        val concatenatedText = "${election.chamberName} ${election.place} ${election.date}"
        textViewTitle.text = concatenatedText*/

        return AlertDialog.Builder(activity).apply {
            setView(binding.root)
            setPositiveButton(resources.getString(R.string.close), null)
        }.create()
    }

    private fun DialogMunicipalitySelectionBinding.setUpViews() {
        dialogMunicipalitySelectionProvinceSpinner.onItemSelectedListener = object: OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val item = parent.getItemAtPosition(position).toString()
                viewModel.requestMunicipalities(item)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun initObservers() { viewModel.viewState.observe(this, ::renderState) }

    private fun renderState(state: MunicipalityState) = when (state) {
        Loading -> Unit
        is ProvincesReceived -> populateProvincesSpinner(state.provinces)
        is Success -> populateMunicipalitiesSpinner(state.municipalities)
        is Failure -> Snackbar.make(binding.root, getString(R.string.something_wrong), Snackbar.LENGTH_LONG).show()
    }

    private fun populateProvincesSpinner(provinces: List<Province>) {
        val provincesNames = provinces.map { it.name }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            provincesNames
        )

        binding.dialogMunicipalitySelectionProvinceSpinner.adapter = adapter
    }

    private fun populateMunicipalitiesSpinner(municipalities: List<Municipality>) {
        val municipalitiesNames = municipalities.map { it.name }

        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            municipalitiesNames
        )

        binding.dialogMunicipalitySelectionMunicipalitySpinner.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
