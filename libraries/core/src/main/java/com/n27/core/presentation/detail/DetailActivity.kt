package com.n27.core.presentation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import android.widget.SimpleAdapter
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.material.progressindicator.LinearProgressIndicator.INDETERMINATE_ANIMATION_TYPE_CONTIGUOUS
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
import com.n27.core.extensions.drawWithResults
import com.n27.core.extensions.playErrorAnimation
import com.n27.core.presentation.common.PresentationUtils
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider
import javax.inject.Inject
import com.n27.core.presentation.detail.DetailState.Loading
import com.n27.core.presentation.detail.DetailState.Failure
import com.n27.core.presentation.detail.DetailState.Success
import com.n27.core.presentation.detail.binders.PartyColorBinder
import java.text.NumberFormat

class DetailActivity : AppCompatActivity() {

    @VisibleForTesting internal lateinit var binding: ActivityDetailBinding
    @VisibleForTesting internal lateinit var currentElection: Election
    @Inject internal lateinit var viewModel: DetailViewModel
    @Inject internal lateinit var utils: PresentationUtils
    internal lateinit var detailComponent: DetailComponent
    internal lateinit var countDownTimer: CountDownTimer
    private lateinit var election: Election
    private var senateElection: Election? = null
    private var liveElectionId: String? = null

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

    private fun requestElection() = viewModel.requestElection(currentElection, liveElectionId)

    private fun Bundle.deserialize() {
        election = getSerializable(KEY_ELECTION) as Election
        currentElection = election
        senateElection = getSerializable(KEY_SENATE_ELECTION) as? Election
        liveElectionId = getString(KEY_ELECTION_ID)
    }

    private fun ActivityDetailBinding.setUpViews() {
        setContentView(root)
        setSupportActionBar(toolbar)
        toolbar.title = generateToolbarTitle()
        initializeCountDownTimer()
        setViewsVisibility(animation = true)
    }

    private fun initObservers() { viewModel.viewState.observe(this, ::renderState) }

    private fun generateToolbarTitle() = "${currentElection.chamberName} (${currentElection.place} " +
            "${currentElection.date})"

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() { binding.pieChart.highlightValue(-1F, -1) }
        }
    }

    private fun setViewsVisibility(
        animation: Boolean = false,
        loading: Boolean = false,
        error: Boolean = false,
        content: Boolean = false
    ) = with(binding) {
        detailLoadingAnimation.isVisible = animation
        progressBar.isVisible = loading
        detailErrorAnimation.isVisible = error
        detailContent.isVisible = content
    }

    private fun renderState(state: DetailState) = when (state) {
        Loading -> showLoading()
        is Success -> showContent(state.election)
        is Failure -> showError(state.error)
    }

    private fun showLoading() = with(binding) {
        when {
            detailErrorAnimation.isVisible -> setViewsVisibility(animation = true)

            !detailLoadingAnimation.isVisible -> setViewsVisibility(
                loading = true,
                content = detailContent.isVisible
            )
        }
    }

    private fun showContent(election: Election) {
        currentElection = election
        binding.setContent()
        setViewsVisibility(content = true)
    }

    private fun showError(errorMsg: String?) = with(binding) {
        if (!detailContent.isVisible) {
            setViewsVisibility(error = true)
            detailErrorAnimation.playErrorAnimation()
        } else {
            setViewsVisibility(content = true)
        }

        val error = when (errorMsg) {
            Constants.NO_INTERNET_CONNECTION -> R.string.no_internet_connection
            else -> R.string.something_wrong
        }

        Snackbar.make(root, getString(error), Snackbar.LENGTH_LONG).show()
    }

    private fun ActivityDetailBinding.setContent() {
        toolbar.title = generateToolbarTitle()

        floatingButtonMoreInfo.setOnClickListener {
            DetailDialog()
                .also { it.arguments = Bundle().apply { putSerializable(KEY_ELECTION, currentElection) } }
                .show(supportFragmentManager, "DetailDialog")

            utils.track("results_info_clicked") {
                param("election", "${currentElection.chamberName} (${currentElection.date})")
            }
        }

        pieChart.drawWithResults(currentElection.results)

        listView.apply {
            adapter = generateResultsAdapter().apply { viewBinder = PartyColorBinder() }

            setOnItemClickListener { _, _, position, _ ->
                pieChart.highlightValue(position.toFloat(), 0)
                countDownTimer.start()
                utils.track("party_clicked") { param("party", currentElection.results[position].party.name) }
            }
        }
    }

    private fun generateResultsAdapter(): SimpleAdapter {
        val keys = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val resources = intArrayOf(
            R.id.tvPartyColor,
            R.id.tvPartyName,
            R.id.tvNumberVotes,
            R.id.tvVotesPercentage,
            R.id.tvElects
        )

        val arrayList = ArrayList<Map<String, Any>>()

        with(currentElection) {
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

        return SimpleAdapter(this, arrayList, R.layout.list_item_detail_activity, keys, resources)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_activity, menu)

        menu.apply {
            findItem(R.id.action_swap).isVisible = senateElection != null
            findItem(R.id.action_reload).isVisible = liveElectionId != null
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_swap -> {
                when (currentElection.chamberName) {
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
