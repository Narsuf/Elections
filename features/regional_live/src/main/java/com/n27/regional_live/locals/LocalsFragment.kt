package com.n27.regional_live.locals

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
import com.n27.core.Constants.KEY_REGION
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.data.local.json.models.Region
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.locals.LocalsState.*
import com.n27.regional_live.locals.adapters.LocalsCardAdapter
import com.n27.regional_live.locals.dialog.MunicipalitySelectionDialog
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.localsRecyclerView.apply { layoutManager = LinearLayoutManager(context) }
        initObservers()
        viewModel.requestRegions()
    }

    private fun initObservers() {
        viewModel.viewState.observeOnLifecycle(
            viewLifecycleOwner,
            distinctUntilChanged = true,
            action = ::renderState
        )
    }

    private fun renderState(state: LocalsState) = when (state) {
        Loading -> Unit
        is Regions -> generateCards(state.regions)
        is ElectionResult -> navigateToDetail(state)
        is Failure -> showError(state.error)
    }

    private fun setViewsVisibility(
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        localsErrorAnimation.isVisible = error
        localsRecyclerView.isVisible = content
    }

    private fun generateCards(regions: List<Region>) {
        setViewsVisibility(content = true)
        binding.localsRecyclerView.adapter = LocalsCardAdapter(regions) { region ->
            MunicipalitySelectionDialog()
                .also { it.arguments = Bundle().apply { putSerializable(KEY_REGION, region) } }
                .show(parentFragmentManager, "MunicipalitySelectionDialog")
        }
    }

    private fun navigateToDetail(state: ElectionResult) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(Constants.KEY_ELECTION, state.election)
            putExtra(Constants.KEY_LOCAL_ELECTION_IDS, state.ids)
        }

        startActivity(intent)
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!localsRecyclerView.isVisible) {
            setViewsVisibility(error = true)
            localsErrorAnimation.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
