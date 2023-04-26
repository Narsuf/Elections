package com.n27.regional_live.regionals

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.data.remote.api.models.ElectionXml
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentRegionalsBinding
import com.n27.regional_live.regionals.adapters.RegionalCardAdapter
import com.n27.regional_live.regionals.models.RegionalsAction
import com.n27.regional_live.regionals.models.RegionalsAction.ShowErrorSnackbar
import com.n27.regional_live.regionals.models.RegionalsContentState
import com.n27.regional_live.regionals.models.RegionalsContentState.Empty
import com.n27.regional_live.regionals.models.RegionalsContentState.WithData
import com.n27.regional_live.regionals.models.RegionalsState
import com.n27.regional_live.regionals.models.RegionalsState.Content
import com.n27.regional_live.regionals.models.RegionalsState.Error
import com.n27.regional_live.regionals.models.RegionalsState.Loading
import javax.inject.Inject

class RegionalsFragment : Fragment() {

    private var _binding: FragmentRegionalsBinding? = null
    @VisibleForTesting internal val binding get() = _binding!!
    @Inject internal lateinit var viewModel: RegionalsViewModel
    @Inject internal lateinit var utils: PresentationUtils

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
        viewModel.requestElections()
    }

    private fun FragmentRegionalsBinding.setUpViews() {
        swipeFragmentRegionals.setOnRefreshListener {
            viewModel.requestElections()
            utils.track("regionals_fragment_pulled_to_refresh")
        }
        recyclerFragmentRegionals.apply { layoutManager = LinearLayoutManager(context) }
    }

    private fun initObservers() {
        viewModel.viewContentState.observeOnLifecycle(
            viewLifecycleOwner,
            distinctUntilChanged = true,
            action = ::renderContentState
        )
        viewModel.viewState.observeOnLifecycle(
            viewLifecycleOwner,
            distinctUntilChanged = true,
            action = ::renderState
        )
        viewModel.viewAction.observeOnLifecycle(
            lifecycleOwner = viewLifecycleOwner,
            action = ::handleAction
        )
    }

    @VisibleForTesting
    internal fun renderContentState(state: RegionalsContentState) = when (state) {
        Empty -> Unit
        is WithData -> generateCards(state)
    }

    private fun generateCards(content: WithData) {
        binding.recyclerFragmentRegionals.adapter = RegionalCardAdapter(
            content.elections,
            content.parties,
            ::navigateToDetail
        )

        utils.track("regionals_fragment_content_loaded")
    }

    @VisibleForTesting
    internal fun navigateToDetail(election: ElectionXml) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(KEY_ELECTION_ID, election.id)
        }

        startActivity(intent)

        utils.track("regionals_fragment_region_clicked") { param("region", election.place) }
    }

    @VisibleForTesting
    internal fun renderState(state: RegionalsState) = when (state) {
        Loading -> Unit
        Content ->  setViewsVisibility(content = true)
        is Error -> showError(state.error)
    }

    private fun setViewsVisibility(
        initialLoading: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        regionalsLoadingAnimation.isVisible = initialLoading
        swipeFragmentRegionals.isRefreshing = loading
        regionalsErrorAnimation.isVisible = error
        recyclerFragmentRegionals.isVisible = content
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.regionalsErrorAnimation.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun handleAction(action: RegionalsAction) = when(action) {
        is ShowErrorSnackbar -> showSnackbar(action.error)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
