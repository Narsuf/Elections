package com.n27.regional_live.ui.regional_live.regionals

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.data.models.Election
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import com.n27.regional_live.ui.regional_live.adapters.RegionalCardAdapter
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.ui.regional_live.regionals.RegionalsState.Success
import javax.inject.Inject

class RegionalsFragment : Fragment() {

    private var _binding: FragmentRegionalsBinding? = null
    private val binding get() = _binding!!
    @Inject internal lateinit var viewModel: RegionalsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegionalsBinding.inflate(inflater, container, false)

        binding.fragmentRegionalsRecyclerView.apply { layoutManager = LinearLayoutManager(context) }
        initObservers()
        viewModel.requestElections()
        return binding.root
    }

    private fun initObservers() { viewModel.viewState.observe(viewLifecycleOwner, ::renderState) }

    private fun renderState(state: RegionalsState) = when (state) {
        Loading -> binding.fragmentRegionalsText.text = "Loading"
        is Success -> generateCards(state)
        is Failure -> binding.fragmentRegionalsText.text = "Error"
    }

    private fun generateCards(success: Success) = with(binding) {
        fragmentRegionalsText.isVisible = false
        fragmentRegionalsRecyclerView.isVisible = true
        fragmentRegionalsRecyclerView.adapter = RegionalCardAdapter(
            success.elections,
            success.parties
        ) { election, id ->
            navigateToDetail(election, id)
        }
    }

    private fun navigateToDetail(election: Election, id: String?) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION, election)
            putExtra(KEY_ELECTION_ID, id)
        }

        startActivity(intent)
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
