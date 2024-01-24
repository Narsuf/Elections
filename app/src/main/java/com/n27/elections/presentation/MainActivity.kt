package com.n27.elections.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n27.core.BuildConfig
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_GENERAL_LIVE_ELECTION
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.Constants.NO_RESULTS
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.openLink
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.Constants.CONGRESS_LIVE
import com.n27.elections.Constants.REGIONAL_LIVE
import com.n27.elections.ElectionsApplication
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.adapters.ElectionCardAdapter
import com.n27.elections.presentation.entities.MainAction
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.elections.presentation.entities.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.Content
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.Loading
import com.n27.regional_live.presentation.RegionalLiveActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityMainBinding
    @Inject internal lateinit var viewModel: MainViewModel
    @Inject internal lateinit var utils: PresentationUtils
    @Inject internal lateinit var remoteConfig: FirebaseRemoteConfig
    private val recyclerAdapter by lazy { ElectionCardAdapter(::navigateToDetail) }

    @VisibleForTesting
    internal fun navigateToDetail(congressElection: Election, senateElection: Election) {
        utils.track("main_activity_election_clicked") { param("election", congressElection.date) }

        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(KEY_ELECTION, congressElection)
        myIntent.putExtra(KEY_SENATE_ELECTION, senateElection)
        startActivity(myIntent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ElectionsApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.setUpViews()

        remoteConfig.apply {
            fetch().addOnCompleteListener { if (it.isSuccessful) activate() }
        }

        initObservers()
        viewModel.requestElections()
    }

    private fun ActivityMainBinding.setUpViews() {
        setContentView(binding.root)
        recyclerActivityMain.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }

        liveElectionsButtonActivityMain.setOnClickListener {
            when {
                isFeatureEnabled(CONGRESS_LIVE, debugValue = false) -> navigateToGeneralsLive()
                isFeatureEnabled(REGIONAL_LIVE) -> navigateToRegionalsLive()
            }

            utils.track("main_activity_live_button_clicked")
        }

        swipeActivityMain.setOnRefreshListener {
            viewModel.requestElections()
            utils.track("main_activity_pulled_to_refresh")
        }
    }

    private fun navigateToGeneralsLive() {
        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(KEY_GENERAL_LIVE_ELECTION, true)
        startActivity(myIntent)
    }

    private fun navigateToRegionalsLive() {
        val myIntent = Intent(this, RegionalLiveActivity::class.java)
        startActivity(myIntent)
    }

    private fun initObservers() {
        viewModel.viewState.observe(this, ::renderState)
        viewModel.viewAction.observeOnLifecycle(lifecycleOwner = this, action = ::handleAction)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        Loading -> setViewsVisibility(animation = true)
        is Content -> showElections(state)
        is Error -> showError(state.errorMessage)
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        loadingAnimationActivityMain.isVisible = animation
        swipeActivityMain.isRefreshing = loading
        errorAnimationActivityMain.isVisible = error
        recyclerActivityMain.isVisible = content
        liveElectionsButtonActivityMain.isVisible = content &&
                (isFeatureEnabled(REGIONAL_LIVE) || isFeatureEnabled(CONGRESS_LIVE))
    }

    private fun isFeatureEnabled(feature: String, debugValue: Boolean = true) = if (BuildConfig.DEBUG)
        debugValue
    else
        remoteConfig.getBoolean(feature)

    private fun showElections(state: Content) = with(binding) {
        setViewsVisibility(content = true)
        recyclerAdapter.updateItems(state.congressElections, state.senateElections)
        utils.track("main_activity_content_loaded")
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.errorAnimationActivityMain.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_RESULTS -> R.string.preliminary_results_not_available_yet
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun handleAction(action: MainAction) = when (action) {
        is ShowDisclaimer -> Unit //onShowDisclaimer()
        is ShowErrorSnackbar -> showSnackbar(action.error)
    }

    private fun onShowDisclaimer() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.disclaimer))
            .setMessage(
                utils.getTextWithLink(
                    text = getString(R.string.disclaimer_description, getString(R.string.disclaimer_link)),
                    link = getString(R.string.disclaimer_link),
                    onClick = {
                        "https://elecciones.eldiario.es/".let {
                            openLink(it)
                            utils.trackLink(it)
                        }
                    }
                )
            )
            .setPositiveButton(getString(R.string.close), null)
            .setOnDismissListener {
                viewModel.saveFirstLaunchFlag()
                utils.track("main_activity_dialog_dismissed")
            }
            .show()

        (alertDialog.findViewById(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }
}
