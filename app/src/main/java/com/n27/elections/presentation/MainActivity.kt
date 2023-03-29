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
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.ElectionsApplication
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.adapters.GeneralElectionsCardAdapter
import com.n27.elections.presentation.entities.MainEvent
import com.n27.elections.presentation.entities.MainEvent.*
import com.n27.elections.presentation.entities.MainInteraction.*
import com.n27.elections.presentation.entities.MainState
import com.n27.elections.presentation.entities.MainState.*
import com.n27.regional_live.ui.regional_live.RegionalLiveActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityMainBinding
    @Inject internal lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ElectionsApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.setup()
        binding.setUpViews()
        initObservers()
        viewModel.handleInteraction(ScreenOpened)
    }

    private fun ActivityMainBinding.setUpViews() {
        swipeActivityMain.setOnRefreshListener { viewModel.handleInteraction(Refresh) }
        recyclerActivityMain.apply { layoutManager = LinearLayoutManager(context) }
        liveElectionsButtonActivityMain.setOnClickListener { viewModel.handleInteraction(LiveButtonClicked) }
    }

    private fun initObservers() {
        viewModel.viewState.observe(this, ::renderState)
        viewModel.viewEvent.observeOnLifecycle(this, action = ::handleEvent)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        Idle -> Unit
        Loading -> setViewsVisibility(animation = true)
        is Error -> showError(state.errorMessage)
        is Success -> showElections(state)
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
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!recyclerActivityMain.isVisible) {
            setViewsVisibility(error = true)
            errorAnimationActivityMain.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(mainContent, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun showElections(state: Success) = with(binding) {
        swipeActivityMain.isRefreshing = false
        setViewsVisibility(content = true)
        liveElectionsButtonActivityMain.isVisible = true
        recyclerActivityMain.adapter = GeneralElectionsCardAdapter(
            state.elections.filter { it.chamberName == "Congreso" },
            state.elections.filter { it.chamberName == "Senado" },
            state.onElectionClicked
        )
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is ShowDisclaimer -> onShowDisclaimer()
        is NavigateToLive -> onNavigateToLive()
        is NavigateToDetail -> onNavigateToDetail(event)
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
            .setOnDismissListener { viewModel.handleInteraction(DialogDismissed) }
            .show()

        (alertDialog.findViewById(android.R.id.message) as TextView).movementMethod = LinkMovementMethod.getInstance()
    }

    private fun onNavigateToLive() {
        val myIntent = Intent(this, RegionalLiveActivity::class.java)
        startActivity(myIntent)
    }

    private fun onNavigateToDetail(event: NavigateToDetail) {
        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(Constants.KEY_ELECTION, event.congressElection)
        myIntent.putExtra(Constants.KEY_SENATE_ELECTION, event.senateElection)
        startActivity(myIntent)
    }
}
