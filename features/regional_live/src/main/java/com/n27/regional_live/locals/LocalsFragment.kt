package com.n27.regional_live.locals

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
import com.n27.core.Constants.KEY_LOCAL_ELECTION_IDS
import com.n27.core.Constants.KEY_REGION
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.data.local.json.models.Region
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.databinding.FragmentLocalsBinding
import com.n27.regional_live.locals.adapters.LocalsCardAdapter
import com.n27.regional_live.locals.dialog.MunicipalitySelectionDialog
import com.n27.regional_live.locals.models.LocalsAction
import com.n27.regional_live.locals.models.LocalsAction.NavigateToDetail
import com.n27.regional_live.locals.models.LocalsAction.ShowErrorSnackbar
import com.n27.regional_live.locals.models.LocalsState
import com.n27.regional_live.locals.models.LocalsState.Content
import com.n27.regional_live.locals.models.LocalsState.Error
import com.n27.regional_live.locals.models.LocalsState.Loading
import javax.inject.Inject

class LocalsFragment : Fragment() {

    private var _binding: FragmentLocalsBinding? = null
    @VisibleForTesting internal val binding get() = _binding!!
    @Inject internal lateinit var viewModel: LocalsViewModel
    @Inject internal lateinit var utils: PresentationUtils

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
        binding.recyclerFragmentLocals.apply { layoutManager = LinearLayoutManager(context) }
        initObservers()
        viewModel.requestRegions()
    }

    private fun initObservers() {
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
    internal fun renderState(state: LocalsState) = when (state) {
        Loading -> Unit
        is Content -> generateCards(state.regions)
        is Error -> showError(state.error)
    }

    private fun generateCards(regions: List<Region>) {
        setViewsVisibility(content = true)
        binding.recyclerFragmentLocals.adapter = LocalsCardAdapter(regions) { region ->
            MunicipalitySelectionDialog()
                .also { it.arguments = Bundle().apply { putSerializable(KEY_REGION, region) } }
                .show(parentFragmentManager, "MunicipalitySelectionDialog")

            utils.track("locals_fragment_region_clicked") { param("region", region.name) }
        }

        utils.track("locals_fragment_content_loaded")
    }

    private fun setViewsVisibility(
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        errorFragmentLocals.isVisible = error
        recyclerFragmentLocals.isVisible = content
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.errorFragmentLocals.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun handleAction(action: LocalsAction) = when (action) {
        is NavigateToDetail -> navigateToDetail(action.ids)
        is ShowErrorSnackbar -> showSnackbar(action.error)
    }

    private fun navigateToDetail(ids: LocalElectionIds) {
        val intent = Intent(activity, DetailActivity::class.java).apply {
            putExtra(KEY_LOCAL_ELECTION_IDS, ids)
        }

        startActivity(intent)

        utils.track("locals_fragment_municipality_selected") {
            param("ids", "${ids.region}/${ids.province}/${ids.municipality}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
