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
import com.n27.elections.presentation.entities.MainAction
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.elections.presentation.entities.MainAction.ShowErrorSnackbar
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.Error
import com.n27.elections.presentation.entities.MainState.InitialLoading
import com.n27.elections.presentation.entities.MainState.Loading
import com.n27.elections.presentation.entities.MainState.Content
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
        viewModel.requestElections(initialLoading = true)
    }

    private fun ActivityMainBinding.setUpViews() {
        setContentView(binding.root)
        swipeActivityMain.setOnRefreshListener { viewModel.requestElections() }
        recyclerActivityMain.apply { layoutManager = LinearLayoutManager(context) }
        liveElectionsButtonActivityMain.setOnClickListener { navigateToLive() }
    }

    private fun navigateToLive() {
        utils.track("live_button_clicked")

        val myIntent = Intent(this, RegionalLiveActivity::class.java)
        startActivity(myIntent)
    }

    private fun initObservers() {
        viewModel.viewState.observeOnLifecycle(
            lifecycleOwner = this,
            distinctUntilChanged = true,
            action = ::renderState
        )
        viewModel.viewAction.observeOnLifecycle(lifecycleOwner = this, action = ::handleAction)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        InitialLoading -> setViewsVisibility(initialLoading = true)
        Loading -> Unit
        is Error -> showError(state.errorMessage)
        is Content -> showElections(state)
    }

    private fun setViewsVisibility(
        initialLoading: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        loadingAnimationActivityMain.isVisible = initialLoading
        swipeActivityMain.isRefreshing = loading
        errorAnimationActivityMain.isVisible = error
        recyclerActivityMain.isVisible = content
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.errorAnimationActivityMain.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> com.n27.core.R.string.no_internet_connection
            else -> com.n27.core.R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun showElections(state: Content) = with(binding) {
        swipeActivityMain.isRefreshing = false
        setViewsVisibility(content = true)
        liveElectionsButtonActivityMain.isVisible = true
        recyclerActivityMain.adapter = GeneralElectionsCardAdapter(
            state.elections.filter { it.chamberName == "Congreso" },
            state.elections.filter { it.chamberName == "Senado" },
            ::navigateToDetail
        )

        utils.track("main_activity_loaded") { param("state", "success") }
    }

    @VisibleForTesting
    internal fun navigateToDetail(congressElection: Election, senateElection: Election) {
        utils.track("election_clicked") { param("election", congressElection.date) }

        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(Constants.KEY_ELECTION, congressElection)
        myIntent.putExtra(Constants.KEY_SENATE_ELECTION, senateElection)
        startActivity(myIntent)
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
                utils.track("dialog_dismissed")
            }
            .show()

        (alertDialog.findViewById(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }
}
