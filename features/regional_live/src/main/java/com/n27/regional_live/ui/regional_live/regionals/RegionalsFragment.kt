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
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.extensions.playErrorAnimation
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegionalsBinding.inflate(inflater, container, false)

        binding.setUpViews()
        initObservers()
        viewModel.requestElections(initialLoading = true)
        return binding.root
    }

    private fun FragmentRegionalsBinding.setUpViews() {
        regionalsSwipe.setOnRefreshListener { viewModel.requestElections() }
        regionalsRecyclerView.apply { layoutManager = LinearLayoutManager(context) }
    }

    private fun initObservers() { viewModel.viewState.observe(viewLifecycleOwner, ::renderState) }

    private fun renderState(state: RegionalsState) = when (state) {
        Loading -> setViewsVisibility(animation = true)
        is Success -> generateCards(state)
        is Failure -> showError(state.throwable?.message)
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        swipe: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        regionalsLoadingAnimation.isVisible = animation
        regionalsSwipe.isRefreshing = swipe
        regionalsErrorAnimation.isVisible = error
        regionalsRecyclerView.isVisible = content
    }

    private fun generateCards(success: Success) {
        setViewsVisibility(content = true)
        binding.regionalsRecyclerView.adapter = RegionalCardAdapter(
            success.elections,
            success.parties
        ) { election, id ->
            navigateToDetail(election, id)
        }
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!regionalsRecyclerView.isVisible) {
            setViewsVisibility(error = true)
            regionalsErrorAnimation.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            Constants.NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun navigateToDetail(election: Election, id: String?) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION, election)
            putExtra(KEY_ELECTION_ID, id)
        }

        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
