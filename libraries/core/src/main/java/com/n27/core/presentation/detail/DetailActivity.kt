package com.n27.core.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.databinding.ActivityDetailBinding
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider
import javax.inject.Inject
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.Success

class DetailActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityDetailBinding
    @VisibleForTesting internal lateinit var currentElection: Election
    @Inject internal lateinit var viewModel: DetailViewModel
    internal lateinit var detailComponent: DetailComponent
    private lateinit var election: Election
    private var senateElection: Election? = null
    private var liveElectionId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        detailComponent = (applicationContext as DetailComponentProvider).provideDetailComponent()
        detailComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        binding.setUpViews()

        intent.extras?.deserialize()

        title = generateToolbarTitle()

        initObservers()
        viewModel.requestElection(election, liveElectionId)
    }

    private fun ActivityDetailBinding.setUpViews() {
        setContentView(root)
        setSupportActionBar(toolbar)

        if (liveElectionId != null)
            detailSwipe.setOnRefreshListener { viewModel.requestElection(election, liveElectionId) }
        else
            detailSwipe.isEnabled = false
    }

    private fun Bundle.deserialize() {
        election = getSerializable(KEY_ELECTION) as Election
        currentElection = election
        senateElection = getSerializable(KEY_SENATE_ELECTION) as? Election
        liveElectionId = getString(KEY_ELECTION_ID)
    }

    private fun generateToolbarTitle() = "${currentElection.chamberName} (${currentElection.place} " +
            "${currentElection.date})"

    private fun initObservers() { viewModel.viewState.observe(this, ::renderState) }

    private fun renderState(state: DetailState) = when (state) {
        Loading -> setViewsVisibility(loading = true)
        is Success -> showContent(state.election)
        is Failure -> showError(state.error)
    }

    private fun setViewsVisibility(
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        detailLoadingAnimation.isVisible = loading
        detailErrorAnimation.isVisible = error
        detailFrame.isVisible = content
    }

    private fun showContent(election: Election) {
        binding.detailSwipe.isRefreshing = false
        setViewsVisibility(content = true)
        currentElection = election
        beginTransactionToDetailFragment()
    }

    private fun beginTransactionToDetailFragment() {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        detailFragment.arguments = Bundle().apply { putSerializable(KEY_ELECTION, currentElection) }

        transaction.replace(R.id.detailFrame, detailFragment)
        transaction.commit()

        binding.toolbar.title = generateToolbarTitle()
    }

    private fun showError(errorMsg: String?) = with(binding) {
        detailSwipe.isRefreshing = false
        setViewsVisibility(error = true)
        detailErrorAnimation.playErrorAnimation()

        val error = when (errorMsg) {
            Constants.NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(detailContent, getString(error), Snackbar.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return if (senateElection != null) {
            menuInflater.inflate(R.menu.menu_detail_activity, menu)
             true
        } else {
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_swap -> {
                if (currentElection.chamberName == KEY_SENATE) {
                    currentElection = election
                    beginTransactionToDetailFragment()
                } else if (currentElection.chamberName == KEY_CONGRESS) {
                    senateElection?.let {
                        currentElection = it
                        beginTransactionToDetailFragment()
                    }
                }

                true
            }

            else -> false
        }
    }
}
