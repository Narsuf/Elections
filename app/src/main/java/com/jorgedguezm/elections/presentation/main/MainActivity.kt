package com.jorgedguezm.elections.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.databinding.ActivityMainBinding
import com.jorgedguezm.elections.presentation.common.Constants
import com.jorgedguezm.elections.presentation.common.extensions.observeOnLifecycle
import com.jorgedguezm.elections.presentation.common.inheritance.ViewModelActivity
import com.jorgedguezm.elections.presentation.detail.DetailActivity
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter
import com.jorgedguezm.elections.presentation.main.entities.MainEvent
import com.jorgedguezm.elections.presentation.main.entities.MainEvent.NavigateToDetail
import com.jorgedguezm.elections.presentation.main.entities.MainInteraction.ScreenOpened
import com.jorgedguezm.elections.presentation.main.entities.MainState
import com.jorgedguezm.elections.presentation.main.entities.MainState.Error
import com.jorgedguezm.elections.presentation.main.entities.MainState.Idle
import com.jorgedguezm.elections.presentation.main.entities.MainState.Loading
import com.jorgedguezm.elections.presentation.main.entities.MainState.Success
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

        binding.recyclerView.apply { layoutManager = LinearLayoutManager(context) }

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

    private fun initObservers() {
        vm.viewState.observe(this, ::renderState)
        vm.viewEvent.observeOnLifecycle(this, action = ::handleEvent)
    }

    @VisibleForTesting
    internal fun renderState(state: MainState) = when (state) {
        Idle -> Unit
        Loading -> Unit
        is Error -> showError()
        is Success -> showElections(state)
    }

    private fun showError() {
        Snackbar.make(binding.content, getString(R.string.something_wrong), Snackbar.LENGTH_LONG).show()
    }

    private fun showElections(state: Success) {
        generalCardAdapter.apply {
            elections = state.elections
            onElectionClicked = state.onElectionClicked
        }

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