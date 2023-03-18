package com.n27.regional_live.ui.regional_live.regionals

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import javax.inject.Inject
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Success

class RegionalsFragment : Fragment() {

    private var _binding: FragmentRegionalsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    @Inject lateinit var viewModel: RegionalsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegionalsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initObservers()
        viewModel.requestElections()
        return root
    }

    private fun initObservers() { viewModel.viewState.observe(viewLifecycleOwner, ::renderState) }

    private fun renderState(state: RegionalsState) = when (state) {
        Loading -> binding.textHome.text = "Loading"
        is Success -> paintACs(state)
        is Failure -> showError(state)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
