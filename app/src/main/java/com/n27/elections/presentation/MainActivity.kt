package com.n27.elections.presentation

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n27.core.BuildConfig
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_GENERAL_LIVE_ELECTION
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.components.Theme
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.openLink
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import com.n27.elections.Constants.CONGRESS_LIVE
import com.n27.elections.Constants.REGIONAL_LIVE
import com.n27.elections.ElectionsApplication
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.entities.MainAction
import com.n27.elections.presentation.entities.MainAction.ShowDisclaimer
import com.n27.regional_live.presentation.RegionalLiveActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityMainBinding
    @Inject internal lateinit var viewModel: MainViewModel
    @Inject internal lateinit var utils: PresentationUtils
    @Inject internal lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as ElectionsApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.setUpViews()

        remoteConfig.apply {
            fetch().addOnCompleteListener { if (it.isSuccessful) activate() }
        }

        viewModel.viewAction.observeOnLifecycle(lifecycleOwner = this, action = ::handleAction)
    }

    private fun ActivityMainBinding.setUpViews() {
        setContentView(binding.root)

        composeViewActivityMain.setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            Theme {
                MainScreen(
                    uiState,
                    onPullToRefresh = {
                        viewModel.requestElections()
                        utils.track("main_activity_pulled_to_refresh")
                    },
                    onElectionClicked = { congressElection, senateElection ->
                        navigateToDetail(congressElection, senateElection)
                    },
                    isLiveButtonVisible = isFeatureEnabled(REGIONAL_LIVE) || isFeatureEnabled(
                        CONGRESS_LIVE
                    ),
                    onLiveClicked = {
                        when {
                            isFeatureEnabled(CONGRESS_LIVE) -> navigateToGeneralsLive()
                            isFeatureEnabled(REGIONAL_LIVE) -> navigateToRegionalsLive()
                        }

                        utils.track("main_activity_live_button_clicked")
                    }
                )
            }
        }
    }

    @VisibleForTesting
    internal fun navigateToDetail(congressElection: Election, senateElection: Election) {
        utils.track("main_activity_election_clicked") { param("election", congressElection.date) }

        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(KEY_ELECTION, congressElection)
        myIntent.putExtra(KEY_SENATE_ELECTION, senateElection)
        startActivity(myIntent)
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

    private fun isFeatureEnabled(feature: String, debugValue: Boolean = true) = if (BuildConfig.DEBUG)
        debugValue
    else
        remoteConfig.getBoolean(feature)

    private fun handleAction(action: MainAction) = when (action) {
        is ShowDisclaimer -> onShowDisclaimer()
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

        alertDialog.findViewById<TextView>(android.R.id.message).movementMethod = LinkMovementMethod.getInstance()
    }
}
