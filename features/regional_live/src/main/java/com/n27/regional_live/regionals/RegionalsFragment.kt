package com.n27.regional_live.regionals

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
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.regionals.RegionalsState.Failure
import com.n27.regional_live.regionals.RegionalsState.InitialLoading
import com.n27.regional_live.regionals.RegionalsState.Loading
import com.n27.regional_live.regionals.RegionalsState.Success
import com.n27.regional_live.regionals.adapters.RegionalCardAdapter
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.setUpViews()
        initObservers()
        viewModel.requestElections(initialLoading = true)
    }

    private fun FragmentRegionalsBinding.setUpViews() {
        regionalsSwipe.setOnRefreshListener { viewModel.requestElections() }
        regionalsRecyclerView.apply { layoutManager = LinearLayoutManager(context) }
    }

    private fun initObservers() {
        viewModel.viewState.observeOnLifecycle(
            viewLifecycleOwner,
            distinctUntilChanged = true,
            action = ::renderState
        )
    }

    private fun renderState(state: RegionalsState) = when (state) {
        InitialLoading -> setViewsVisibility(initialLoading = true)
        Loading -> Unit
        is Success -> generateCards(state)
        is Failure -> showError(state.throwable?.message)
    }

    private fun setViewsVisibility(
        initialLoading: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        regionalsLoadingAnimation.isVisible = initialLoading
        regionalsSwipe.isRefreshing = loading
        regionalsErrorAnimation.isVisible = error
        regionalsRecyclerView.isVisible = content
    }

    private fun generateCards(success: Success) {
        setViewsVisibility(content = true)
        binding.regionalsRecyclerView.adapter = RegionalCardAdapter(
            success.elections,
            success.parties,
            ::navigateToDetail
        )
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!regionalsRecyclerView.isVisible) {
            setViewsVisibility(error = true)
            regionalsErrorAnimation.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
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
