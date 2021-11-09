package com.jorgedguezm.elections.view.ui.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.databinding.FragmentDetailBinding
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.utils.Constants.KEY_ELECTION
import com.jorgedguezm.elections.utils.Constants.KEY_SENATE
import com.jorgedguezm.elections.view.binders.PartyColorBinder
import com.jorgedguezm.elections.view.ui.ViewModelFragment

class DetailFragment : ViewModelFragment() {

    internal lateinit var binding: FragmentDetailBinding
    internal lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = binding(inflater, R.layout.fragment_detail, container)

        val election = arguments?.getSerializable(KEY_ELECTION) as Election

        binding.floatingButtonMoreInfo.setOnClickListener {
            val bundle = Bundle()
            val dialog = DetailDialog()

            bundle.putSerializable(KEY_ELECTION, election)
            dialog.arguments = bundle
            activity?.supportFragmentManager?.let { dialog.show(it, "DetailDialog") }
        }

        utils.drawPieChart(binding.pieChart, election.results)
        initializeCountDownTimer()

        // Manage list view with election data.
        val resultsAdapter = getResultsAdapter(election)
        resultsAdapter.viewBinder = PartyColorBinder()

        binding.listView.adapter = resultsAdapter
        binding.listView.setOnItemClickListener { _, _, position, _ ->
            binding.pieChart.highlightValue(position.toFloat(), 0)
            countDownTimer.start()
        }

        return binding.root
    }

    private fun getResultsAdapter(election: Election): SimpleAdapter {
        val from = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val to = intArrayOf(R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes,
                R.id.tvVotesPercentage, R.id.tvElects)

        val arrayList = ArrayList<Map<String, Any>>()
        val sortedResults = election.results.sortedByDescending { it.elects }

        for (r in sortedResults) {
            val map = HashMap<String, Any>()

            map[from[0]] = "#" + r.party.color
            map[from[1]] = r.party.name
            map[from[2]] = r.votes
            map[from[3]] = if (election.chamberName == KEY_SENATE)
                "- %"
            else
                utils.getPercentageWithTwoDecimals(r.votes, election.validVotes).toString() + " %"

            map[from[4]] = r.elects

            arrayList.add(map)
        }

        return SimpleAdapter(context, arrayList, R.layout.list_item_detail_activity, from, to)
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() { binding.pieChart.highlightValue(-1F, -1) }
        }
    }
}