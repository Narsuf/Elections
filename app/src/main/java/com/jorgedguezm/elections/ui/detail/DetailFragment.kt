package com.jorgedguezm.elections.ui.detail

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTION
import com.jorgedguezm.elections.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.Utils

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.detail_fragment.*

import javax.inject.Inject

class DetailFragment : Fragment() {

    private lateinit var parties: HashMap<String, String>
    private lateinit var election: Election
    private lateinit var results: ArrayList<Results>

    private lateinit var countDownTimer: CountDownTimer

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        parties = arguments?.getSerializable(KEY_PARTIES) as HashMap<String, String>
        election = arguments?.getSerializable(KEY_ELECTION) as Election
        results = arguments?.getSerializable(KEY_RESULTS) as ArrayList<Results>
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

        utils.drawPieChart(pie_chart, utils.getElectsFromResults(results),
                utils.getColorsFromResults(results, parties))

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

        for (r in results) {
            val map = HashMap<String, Any>()

            map[from[0]] = "#" + parties[r.partyId]
            map[from[1]] = r.partyId
            map[from[2]] = r.votes
            map[from[3]] = utils.getPercentageWithTwoDecimals(r.votes, election.validVotes)
                    .toString() + " %"

            map[from[4]] = r.elects!!

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

    inner class PartyColorBinder: SimpleAdapter.ViewBinder {

        override fun setViewValue(view: View?, data: Any?, textRepresentation: String?): Boolean {
            if (view is TextView && data.toString().matches(Regex("[#].*"))) {
                view.setBackgroundColor(Color.parseColor(data.toString()))
                return true
            }

            return false
        }
    }
}