package com.n27.core.presentation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_GENERAL_LIVE_ELECTION
import com.n27.core.Constants.KEY_LOCAL_ELECTION_IDS
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.databinding.ActivityDetailBinding
import com.n27.core.domain.election.Election
import com.n27.core.domain.election.Result
import com.n27.core.domain.live.models.LocalElectionIds
import com.n27.core.extensions.drawWithResults
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.injection.CoreComponent
import com.n27.core.injection.CoreComponentProvider
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.adapters.ResultsAdapter
import com.n27.core.presentation.detail.dialog.DetailDialog
import com.n27.core.presentation.detail.entities.DetailAction
import com.n27.core.presentation.detail.entities.DetailAction.Refreshing
import com.n27.core.presentation.detail.entities.DetailAction.ShowErrorSnackbar
import com.n27.core.presentation.detail.entities.DetailFlags
import com.n27.core.presentation.detail.entities.DetailInteraction.Refresh
import com.n27.core.presentation.detail.entities.DetailInteraction.ScreenOpened
import com.n27.core.presentation.detail.entities.DetailInteraction.Swap
import com.n27.core.presentation.detail.entities.DetailState
import com.n27.core.presentation.detail.entities.DetailState.Content
import com.n27.core.presentation.detail.entities.DetailState.Error
import com.n27.core.presentation.detail.entities.DetailState.Loading
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @VisibleForTesting internal var currentElection: Election? = null
    @VisibleForTesting internal lateinit var binding: ActivityDetailBinding
    @Inject internal lateinit var viewModel: DetailViewModel
    @Inject internal lateinit var utils: PresentationUtils
    internal lateinit var coreComponent: CoreComponent
    internal lateinit var countDownTimer: CountDownTimer
    private lateinit var flags: DetailFlags
    private var senateElection: Election? = null
    private val recyclerAdapter by lazy { ResultsAdapter(::onItemClicked) }

    private fun onItemClicked(position: Int, result: Result) {
        binding.pieChartActivityDetail.highlightValue(position.toFloat(), 0)
        countDownTimer.start()
        utils.track("detail_activity_party_clicked") {
            param("party", result.party.name)
            param("seats", result.elects.toString())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        coreComponent = (applicationContext as CoreComponentProvider).provideCoreComponent()
        coreComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        intent.extras?.deserialize()
        binding.setUpViews()
        initObservers()
        viewModel.handleInteraction(ScreenOpened(flags))
    }

    private fun Bundle.deserialize() {
        currentElection = getSerializable(KEY_ELECTION) as? Election
        senateElection = getSerializable(KEY_SENATE_ELECTION) as? Election

        flags = DetailFlags(
            currentElection,
            isLiveGeneralElection = getBoolean(KEY_GENERAL_LIVE_ELECTION),
            liveRegionalElectionId = getString(KEY_ELECTION_ID),
            liveLocalElectionIds = getSerializable(KEY_LOCAL_ELECTION_IDS) as? LocalElectionIds
        )
    }

    private fun ActivityDetailBinding.setUpViews() {
        setContentView(root)
        setSupportActionBar(toolbarActivityDetail)
        generateToolbarTitle()?.let { toolbarActivityDetail.title = it }
        initializeCountDownTimer()

        swipeActivityDetail.apply {
            if (senateElection != null) isEnabled = false

            setOnRefreshListener {
                viewModel.handleInteraction(Refresh(currentElection, flags))
                utils.track("detail_activity_reload")
            }
        }

        moreInfoButtonActivityDetail.setOnClickListener {
            DetailDialog()
                .also { it.arguments = Bundle().apply { putSerializable(KEY_ELECTION, currentElection) } }
                .show(supportFragmentManager, "DetailDialog")

            utils.track("detail_activity_results_info_clicked") {
                param("election", "${currentElection?.chamberName} (${currentElection?.date})")
            }
        }

        listActivityDetail.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recyclerAdapter
        }
    }

    private fun initObservers() {
        viewModel.viewState.observe(this, ::renderState)
        viewModel.viewAction.observe(this, ::handleAction)
    }

    private fun generateToolbarTitle() = currentElection?.let { "${it.chamberName} ${it.place} (${it.date})" }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() { binding.pieChartActivityDetail.highlightValue(-1F, -1) }
        }
    }

    @VisibleForTesting
    internal fun renderState(state: DetailState) = when (state) {
        Loading -> setViewsVisibility(animation = true)
        is Content -> showElections(state)
        is Error -> showError(state.error)
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        loadingAnimationActivityDetail.isVisible = animation
        swipeActivityDetail.isRefreshing = loading
        errorAnimationActivityDetail.isVisible = error
        contentActivityDetail.isVisible = content
    }

    private fun showElections(content: Content) {
        currentElection = content.election
        binding.setContent(content)
        setViewsVisibility(content = true)
        utils.track("detail_activity_content_loaded")
    }

    private fun ActivityDetailBinding.setContent(content: Content) {
        with(content.election) {
            toolbarActivityDetail.title = generateToolbarTitle()
            scrutinizedActivityDetail.text = getString(R.string.scrutinized, scrutinized.toString())
            scrutinizedBarActivityDetail.progress = scrutinized.toInt()
            recyclerAdapter.updateItems(content.election)

            // Looks like there is a charting library's bug in this specific case.
            pieChartActivityDetail.apply {
                drawWithResults(results)
                drawWithResults(results)
            }
        }
    }

    private fun showError(errorMsg: String?) {
        setViewsVisibility(error = true)
        binding.errorAnimationActivityDetail.playErrorAnimation()
        showSnackbar(errorMsg)
    }

    private fun showSnackbar(errorMsg: String?) {
        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(binding.root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    @VisibleForTesting
    internal fun handleAction(action: DetailAction) = when(action) {
        Refreshing -> setViewsVisibility(loading = true, content = true)
        is ShowErrorSnackbar -> hideLoadingAndShowSnackbar(action.error)
    }

    private fun hideLoadingAndShowSnackbar(error: String?) {
        setViewsVisibility(content = true)
        showSnackbar(error)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)
        menu.findItem(R.id.action_swap).isVisible = senateElection != null || flags.isLiveGeneralElection
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_swap -> {
                viewModel.handleInteraction(Swap(senateElection, currentElection, flags))

                utils.track("detail_activity_swap_clicked") {
                    param("from", "${currentElection?.chamberName}")
                }

                true
            }

            else -> false
        }
    }
}
