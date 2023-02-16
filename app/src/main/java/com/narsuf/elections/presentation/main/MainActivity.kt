package com.narsuf.elections.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.annotation.VisibleForTesting
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.narsuf.elections.R
import com.narsuf.elections.databinding.ActivityMainBinding
import com.narsuf.elections.presentation.common.Constants
import com.narsuf.elections.presentation.common.Constants.NO_INTERNET_CONNECTION
import com.narsuf.elections.presentation.common.Constants.SERVER_COMMUNICATION_ISSUES
import com.narsuf.elections.presentation.common.extensions.observeOnLifecycle
import com.narsuf.elections.presentation.common.inheritance.ViewModelActivity
import com.narsuf.elections.presentation.detail.DetailActivity
import com.narsuf.elections.presentation.main.adapters.GeneralCardAdapter
import com.narsuf.elections.presentation.main.entities.MainEvent
import com.narsuf.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.narsuf.elections.presentation.main.entities.MainInteraction.Refresh
import com.narsuf.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.narsuf.elections.presentation.main.entities.MainState
import com.narsuf.elections.presentation.main.entities.MainState.*
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
                SERVER_COMMUNICATION_ISSUES -> R.string.server_communication_issues
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
        is NavigateToDetail -> onNavigateToDetail(event)
    }

    private fun onNavigateToDetail(event: NavigateToDetail) {
        val myIntent = Intent(this, DetailActivity::class.java)
        myIntent.putExtra(Constants.KEY_CONGRESS_ELECTION, event.congressElection)
        myIntent.putExtra(Constants.KEY_SENATE_ELECTION, event.senateElection)
        startActivity(myIntent)
    }
}
