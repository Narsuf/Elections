package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.KEY_ELECTION
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.Utils
import com.jorgedguezm.elections.ui.binders.PartyColorBinder

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.detail_fragment.*

import javax.inject.Inject

class DetailFragment : Fragment() {

    private lateinit var election: Election

    internal lateinit var countDownTimer: CountDownTimer

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        election = arguments?.getSerializable(KEY_ELECTION) as Election
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.detail_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        floating_button_more_info.setOnClickListener {
            val bundle = Bundle()
            val dialog = DetailDialog()

            bundle.putSerializable(KEY_ELECTION, election)
            dialog.arguments = bundle
            dialog.utils = utils
            dialog.show(activity?.supportFragmentManager, "DetailDialog")
        }

        utils.drawPieChart(pie_chart, election.results)

        initializeCountDownTimer()
        setSimpleAdapter()
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {

            override fun onTick(millisUntilFinished: Long) { }

            override fun onFinish() { pie_chart.highlightValue(-1F, -1) }
        }
    }

    private fun setSimpleAdapter() {
        val from = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val to = intArrayOf(R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes,
                R.id.tvVotesPercentage, R.id.tvElects)

        val arrayList = ArrayList<Map<String, Any>>()

        for (r in election.results) {
            val map = HashMap<String, Any>()

            map[from[0]] = "#" + r.party.color
            map[from[1]] = r.party.name
            map[from[2]] = r.votes
            map[from[3]] = utils.getPercentageWithTwoDecimals(r.votes, election.validVotes)
                    .toString() + " %"

            map[from[4]] = r.elects

            arrayList.add(map)
        }

        val adapter = SimpleAdapter(context, arrayList, R.layout.list_item_detail_activity,
                from, to)

        adapter.viewBinder = PartyColorBinder()

        list_view.adapter = adapter

        list_view.setOnItemClickListener { _, _, position, _ ->
            pie_chart.highlightValue(position.toFloat(), 0)
            countDownTimer.start()
        }
    }
}