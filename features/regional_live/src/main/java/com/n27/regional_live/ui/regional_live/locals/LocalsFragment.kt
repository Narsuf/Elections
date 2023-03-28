package com.n27.regional_live.ui.regional_live.locals

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.R
import com.n27.core.data.json.models.Region
import com.n27.core.extensions.playErrorAnimation
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Failure
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Loading
import com.n27.regional_live.ui.regional_live.locals.LocalsState.Success
import com.n27.regional_live.ui.regional_live.locals.adapters.LocalsCardAdapter
import com.n27.regional_live.ui.regional_live.regionals.RegionalsViewModel
import com.n27.regional_live.ui.regional_live.regionals.adapters.RegionalCardAdapter
import javax.inject.Inject

class LocalsFragment : Fragment() {

    private var _binding: FragmentLocalsBinding? = null
    private val binding get() = _binding!!
    @Inject internal lateinit var viewModel: LocalsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as RegionalLiveActivity).regionalLiveComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLocalsBinding.inflate(inflater, container, false)

        binding.localsRecyclerView.apply { layoutManager = LinearLayoutManager(context) }
        initObservers()
        viewModel.requestRegions(initialLoading = true)
        return binding.root
    }

    private fun initObservers() { viewModel.viewState.observe(viewLifecycleOwner, ::renderState) }

    private fun renderState(state: LocalsState) = when (state) {
        Loading -> setViewsVisibility(animation = true)
        is Success -> generateCards(state.regions)
        is Failure -> showError(state.throwable?.message)
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        swipe: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        localsLoadingAnimation.isVisible = animation
        localsSwipe.isRefreshing = swipe
        localsErrorAnimation.isVisible = error
        localsRecyclerView.isVisible = content
    }

    private fun generateCards(regions: List<Region>) {
        setViewsVisibility(content = true)
        binding.localsRecyclerView.adapter = LocalsCardAdapter(
            regions
        ) { }
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!localsRecyclerView.isVisible) {
            setViewsVisibility(error = true)
            localsErrorAnimation.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            Constants.NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
