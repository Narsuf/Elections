package org.n27.elections.presentation.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import org.n27.elections.R
import org.n27.elections.data.models.Election
import org.n27.elections.databinding.FragmentDetailBinding
import org.n27.elections.presentation.common.Constants.KEY_ELECTION
import org.n27.elections.presentation.common.Constants.KEY_SENATE
import org.n27.elections.presentation.common.extensions.track
import org.n27.elections.presentation.common.inheritance.ViewModelFragment
import org.n27.elections.presentation.detail.binders.PartyColorBinder
import java.text.NumberFormat.getIntegerInstance

class DetailFragment : ViewModelFragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    internal val binding get() = _binding!!

    private lateinit var election: Election
    internal lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        election = arguments?.getSerializable(KEY_ELECTION) as Election

        binding.setupViews()
        return binding.root
    }

    private fun FragmentDetailBinding.setupViews() {
        floatingButtonMoreInfo.setOnClickListener {
            val bundle = Bundle()
            val dialog = DetailDialog()

            bundle.putSerializable(KEY_ELECTION, election)
            dialog.arguments = bundle
            activity?.supportFragmentManager?.let { dialog.show(it, "DetailDialog") }

            analytics.track("results_info_clicked") {
                param("election", "${election.chamberName} (${election.date})")
            }
        }

        // Prepare chart
        utils.drawPieChart(pieChart, election.results)
        initializeCountDownTimer()

        // Fill ListView with election data.
        val resultsAdapter = election.generateResultsAdapter()
        resultsAdapter.viewBinder = PartyColorBinder()

        listView.adapter = resultsAdapter
        listView.setOnItemClickListener { _, _, position, _ ->
            pieChart.highlightValue(position.toFloat(), 0)
            countDownTimer.start()

            analytics.track("party_clicked") { param("party", election.results[position].party.name) }
        }
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() { binding.pieChart.highlightValue(-1F, -1) }
        }
    }

    private fun Election.generateResultsAdapter(): SimpleAdapter {
        val from = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val to = intArrayOf(R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes, R.id.tvVotesPercentage,
            R.id.tvElects)

        val arrayList = ArrayList<Map<String, Any>>()
        val sortedResults = results.sortedByDescending { it.elects }

        for (r in sortedResults) {
            val map = HashMap<String, Any>()

            map[from[0]] = "#" + r.party.color
            map[from[1]] = r.party.name
            map[from[2]] = getIntegerInstance().format(r.votes)
            map[from[3]] = if (chamberName == KEY_SENATE)
                "- %"
            else
                utils.getPercentageWithTwoDecimals(r.votes, validVotes).toString() + " %"

            map[from[4]] = r.elects

            arrayList.add(map)
        }

        return SimpleAdapter(context, arrayList, R.layout.list_item_detail_activity, from, to)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        _binding = null
    }
}
