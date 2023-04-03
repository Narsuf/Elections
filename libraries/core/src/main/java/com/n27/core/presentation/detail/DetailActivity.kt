package com.n27.core.presentation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.SimpleAdapter
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.snackbar.Snackbar
import com.n27.core.Constants
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_ELECTION_ID
import com.n27.core.Constants.KEY_LOCAL_ELECTION_IDS
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.Constants.NO_INTERNET_CONNECTION
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.data.remote.api.models.LocalElectionIds
import com.n27.core.databinding.ActivityDetailBinding
import com.n27.core.extensions.drawWithResults
import com.n27.core.extensions.observeOnLifecycle
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.InitialLoading
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Success
import com.n27.core.presentation.detail.binders.PartyColorBinder
import com.n27.core.presentation.detail.dialog.DetailDialog
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider
import java.text.NumberFormat
import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    @VisibleForTesting internal var currentElection: Election? = null
    @VisibleForTesting internal lateinit var binding: ActivityDetailBinding
    @Inject internal lateinit var viewModel: DetailViewModel
    @Inject internal lateinit var utils: PresentationUtils
    internal lateinit var detailComponent: DetailComponent
    internal lateinit var countDownTimer: CountDownTimer

    private var election: Election? = null
    private var senateElection: Election? = null
    private var liveElectionId: String? = null
    private var liveLocalElectionIds: LocalElectionIds? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        detailComponent = (applicationContext as DetailComponentProvider).provideDetailComponent()
        detailComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        intent.extras?.deserialize()
        binding.setUpViews()
        initObservers()
        requestElection()
    }

    private fun Bundle.deserialize() {
        election = getSerializable(KEY_ELECTION) as? Election
        currentElection = election
        senateElection = getSerializable(KEY_SENATE_ELECTION) as? Election
        liveElectionId = getString(KEY_ELECTION_ID)
        liveLocalElectionIds = getSerializable(KEY_LOCAL_ELECTION_IDS) as? LocalElectionIds
    }

    private fun ActivityDetailBinding.setUpViews() {
        setContentView(root)
        setSupportActionBar(toolbarActivityDetail)
        generateToolbarTitle()?.let { toolbarActivityDetail.title = it }
        initializeCountDownTimer()
    }

    private fun initObservers() {
        viewModel.viewState.observeOnLifecycle(
            lifecycleOwner = this,
            distinctUntilChanged = true,
            action = ::renderState
        )
    }

    private fun requestElection() { viewModel.requestElection(currentElection, liveElectionId, liveLocalElectionIds) }

    private fun generateToolbarTitle() = currentElection?.let { "${it.chamberName} (${it.place} ${it.date})" }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() { binding.pieChartActivityDetail.highlightValue(-1F, -1) }
        }
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        loadingAnimationActivityDetail.isVisible = animation
        progressBarActivityDetail.isVisible = loading
        errorAnimationActivityDetail.isVisible = error
        contentActivityDetail.isVisible = content
    }

    @VisibleForTesting
    internal fun renderState(state: DetailState) = when (state) {
        InitialLoading -> setViewsVisibility(animation = true)
        Loading -> showLoading()
        is Success -> showContent(state.election)
        is Failure -> showError(state.error)
    }

    private fun showLoading() = with(binding) {
        when {
            errorAnimationActivityDetail.isVisible -> setViewsVisibility(animation = true)
            !loadingAnimationActivityDetail.isVisible -> setViewsVisibility(
                loading = true,
                content = contentActivityDetail.isVisible
            )
        }
    }

    private fun showContent(election: Election) {
        currentElection = election
        binding.setContent(election)
        setViewsVisibility(content = true)
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!contentActivityDetail.isVisible) {
            setViewsVisibility(error = true)
            errorAnimationActivityDetail.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun ActivityDetailBinding.setContent(election: Election) {
        toolbarActivityDetail.title = generateToolbarTitle()

        moreInfoButtonActivityDetail.setOnClickListener {
            DetailDialog()
                .also { it.arguments = Bundle().apply { putSerializable(KEY_ELECTION, currentElection) } }
                .show(supportFragmentManager, "DetailDialog")

            utils.track("results_info_clicked") {
                param("election", "${election.chamberName} (${election.date})")
            }
        }

        pieChartActivityDetail.drawWithResults(election.results)

        listActivityDetail.apply {
            adapter = generateResultsAdapter(election).apply { viewBinder = PartyColorBinder() }

            setOnItemClickListener { _, _, position, _ ->
                pieChartActivityDetail.highlightValue(position.toFloat(), 0)
                countDownTimer.start()
                utils.track("party_clicked") {
                    param("party", election.results[position].party.name)
                }
            }
        }
    }

    private fun generateResultsAdapter(election: Election): SimpleAdapter {
        val keys = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val resources = intArrayOf(
            R.id.color_list_item_activity_detail,
            R.id.party_list_item_activity_detail,
            R.id.votes_list_item_activity_detail,
            R.id.percentage_list_item_activity_detail,
            R.id.elects_list_item_activity_detail
        )

        val arrayList = ArrayList<Map<String, Any>>()

        with(election) {
            for (r in results) {
                val map = HashMap<String, Any>()

                map[keys[0]] = "#" + r.party.color
                map[keys[1]] = r.party.name
                map[keys[2]] = NumberFormat.getIntegerInstance().format(r.votes)
                map[keys[3]] = if (chamberName == KEY_SENATE)
                    "- %"
                else
                    utils.getPercentageWithTwoDecimals(r.votes, validVotes) + " %"

                map[keys[4]] = r.elects

                arrayList.add(map)
            }
        }

        return SimpleAdapter(this, arrayList, R.layout.list_item_activity_detail, keys, resources)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_detail, menu)

        menu.apply {
            findItem(R.id.action_swap).isVisible = senateElection != null
            findItem(R.id.action_reload).isVisible =
                liveElectionId != null || liveLocalElectionIds != null
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_swap -> {
                when (currentElection?.chamberName) {
                    KEY_SENATE -> {
                        currentElection = election
                        requestElection()
                    }

                    KEY_CONGRESS -> {
                        senateElection?.let {
                            currentElection = it
                            requestElection()
                        }
                    }
                }

                true
            }

            R.id.action_reload -> {
                requestElection()
                true
            }

            else -> false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}
