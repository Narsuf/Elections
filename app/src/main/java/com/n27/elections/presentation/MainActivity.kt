package com.n27.elections.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.data.models.Election
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.ElectionsApplication
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.adapters.GeneralElectionsCardAdapter
import com.n27.elections.presentation.models.MainAction
import com.n27.elections.presentation.models.MainAction.ShowDisclaimer
import com.n27.elections.presentation.models.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.models.MainContentState
import com.n27.elections.presentation.models.MainContentState.Empty
import com.n27.elections.presentation.models.MainContentState.WithData
import com.n27.elections.presentation.models.MainState
import com.n27.elections.presentation.models.MainState.Content
import com.n27.elections.presentation.models.MainState.Error
import com.n27.elections.presentation.models.MainState.Loading
import com.n27.regional_live.RegionalLiveActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityMainBinding
    @Inject internal lateinit var viewModel: MainViewModel
    @Inject internal lateinit var utils: PresentationUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ElectionsApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.setUpViews()
        initObservers()
        viewModel.requestElections()
    }

    private fun ActivityMainBinding.setUpViews() {
        setContentView(binding.root)
        swipeActivityMain.setOnRefreshListener {
            viewModel.requestElections()
            utils.track("main_activity_pulled_to_refresh")
        }
        recyclerActivityMain.apply { layoutManager = LinearLayoutManager(context) }
        liveElectionsButtonActivityMain.setOnClickListener { navigateToLive() } // TODO: Set feature flag and tooltip.
    }

    private fun navigateToLive() {
        utils.track("main_activity_live_button_clicked")

        val myIntent = Intent(this, RegionalLiveActivity::class.java)
        startActivity(myIntent)
    }

    private fun initObservers() {
        viewModel.viewContentState.observeOnLifecycle(
            lifecycleOwner = this,
            distinctUntilChanged = true,
            action = ::renderContentState
        )
        viewModel.viewState.observeOnLifecycle(
            lifecycleOwner = this,
            distinctUntilChanged = true,
            action = ::renderState
        )
        viewModel.viewAction.observeOnLifecycle(lifecycleOwner = this, action = ::handleAction)
    }

    @VisibleForTesting
    internal fun renderContentState(state: MainContentState) = when (state) {
        Empty -> Unit
        is WithData -> showElections(state.elections)
    }

    private fun showElections(elections: List<Election>) = with(binding) {
        recyclerActivityMain.adapter = GeneralElectionsCardAdapter(
            elections.filter { it.chamberName == "Congreso" },
            elections.filter { it.chamberName == "Senado" },
            ::navigateToDetail
        )

        utils.track("main_activity_content_loaded")
    }

    @VisibleForTesting
    internal fun navigateToDetail(congressElection: Election, senateElection: Election) {
        utils.track("main_activity_election_clicked") { param("election", congressElection.date) }

        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(Constants.KEY_ELECTION, congressElection)
        myIntent.putExtra(Constants.KEY_SENATE_ELECTION, senateElection)
        startActivity(myIntent)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        Loading -> Unit
        Content -> setViewsVisibility(content = true)
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
        liveElectionsButtonActivityMain.isVisible = content
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.errorAnimationActivityMain.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun handleAction(action: MainAction) = when (action) {
        is ShowDisclaimer -> onShowDisclaimer()
        is ShowErrorSnackbar -> showSnackbar(action.error)
    }

    private fun onShowDisclaimer() {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(getString(R.string.disclaimer))
            .setMessage(
                HtmlCompat.fromHtml(
                    "${getString(R.string.disclaimer_description)} " +
                            "<a href=\"https://resultados.elpais.com/elecciones/generales.html\">" +
                            "El País" +
                            "</a>.",
                    HtmlCompat.FROM_HTML_MODE_LEGACY
                )
            )
            .setPositiveButton(getString(R.string.disclaimer_button), null)
            .setOnDismissListener {
                viewModel.saveFirstLaunchFlag()
                utils.track("main_activity_dialog_dismissed")
            }
            .show()

        (alertDialog.findViewById(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }
}
