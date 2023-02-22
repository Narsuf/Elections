package com.n27.elections.presentation.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.n27.elections.R
import com.n27.elections.databinding.ActivityMainBinding
import com.n27.elections.presentation.common.Constants
import com.n27.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import com.n27.elections.presentation.common.extensions.observeOnLifecycle
import com.n27.elections.presentation.common.inheritance.ViewModelActivity
import com.n27.elections.presentation.detail.DetailActivity
import com.n27.elections.presentation.main.adapters.GeneralCardAdapter
import com.n27.elections.presentation.main.entities.MainEvent
import com.n27.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.n27.elections.presentation.main.entities.MainEvent.ShowDisclaimer
import com.n27.elections.presentation.main.entities.MainInteraction.DialogDismissed
import com.n27.elections.presentation.main.entities.MainInteraction.Refresh
import com.n27.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.n27.elections.presentation.main.entities.MainState
import com.n27.elections.presentation.main.entities.MainState.Error
import com.n27.elections.presentation.main.entities.MainState.Idle
import com.n27.elections.presentation.main.entities.MainState.Loading
import com.n27.elections.presentation.main.entities.MainState.Success
import javax.inject.Inject

class MainActivity : ViewModelActivity() {

    internal lateinit var binding: ActivityMainBinding
    private val vm by viewModel<MainViewModel>()

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.toolbar.setup()
        binding.setupViews()
        initObservers()
        vm.handleInteraction(ScreenOpened)
    }

    /*private fun Toolbar.setup() {
        inflateMenu(R.menu.menu_main)
        setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_show_historical -> {
                    vm.handleInteraction(ShowHistoric)
                    true
                }

                else -> false
            }
        }
    }*/

    private fun ActivityMainBinding.setupViews() {
        swipe.setOnRefreshListener { vm.handleInteraction(Refresh) }
        recyclerView.apply { layoutManager = LinearLayoutManager(context) }
    }

    private fun initObservers() {
        vm.viewState.observe(this, ::renderState)
        vm.viewEvent.observeOnLifecycle(this, action = ::handleEvent)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        Idle -> Unit
        Loading -> loading()
        is Error -> showError(state)
        is Success -> showElections(state)
    }

    private fun loading() {
        if (dataUtils.isConnectedToInternet()) {
            binding.errorAnimation.visibility = GONE
            binding.loadingAnimation.visibility = VISIBLE
        }
    }

    private fun showError(state: Error) {
        binding.swipe.isRefreshing = false
        binding.recyclerView.visibility = GONE
        binding.loadingAnimation.visibility = GONE
        with(binding.errorAnimation) {
            visibility = VISIBLE
            playAnimation()
            addAnimatorUpdateListener {
                val progress = it.animatedFraction

                if (progress in 0.67F..0.68F) {
                    removeAllUpdateListeners()
                    pauseAnimation()
                }
            }

            val error = when (state.errorMessage) {
                NO_INTERNET_CONNECTION -> R.string.no_internet_connection
                else -> R.string.something_wrong
            }

            Snackbar.make(binding.content, getString(error), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showElections(state: Success) {
        generalCardAdapter.apply {
            elections = state.elections
            onElectionClicked = state.onElectionClicked
        }

        binding.swipe.isRefreshing = false
        binding.errorAnimation.visibility = GONE
        binding.loadingAnimation.visibility = GONE
        binding.recyclerView.visibility = VISIBLE
        binding.recyclerView.adapter = generalCardAdapter
    }

    private fun handleEvent(event: MainEvent) = when (event) {
        is ShowDisclaimer -> onShowDisclaimer()
        is NavigateToDetail -> onNavigateToDetail(event)
    }

    private fun onShowDisclaimer() {
        AlertDialog.Builder(this)
            .setTitle("Disclaimer")
            .setMessage("Blablablabla")
            .setPositiveButton("Accept", null)
            .setOnDismissListener { vm.handleInteraction(DialogDismissed) }
            .show()
    }

    private fun onNavigateToDetail(event: NavigateToDetail) {
        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(Constants.KEY_CONGRESS_ELECTION, event.congressElection)
        myIntent.putExtra(Constants.KEY_SENATE_ELECTION, event.senateElection)
        startActivity(myIntent)
    }
}
