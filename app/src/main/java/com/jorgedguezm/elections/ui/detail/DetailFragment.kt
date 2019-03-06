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
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_ELECTION
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.utils.Utils

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.detail_fragment.*

import java.math.RoundingMode

import javax.inject.Inject

class DetailFragment : Fragment() {

    lateinit var parties: HashMap<String, String>
    lateinit var election: Election
    lateinit var results: ArrayList<Results>

    lateinit var countDownTimer: CountDownTimer

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

        for (i in results.indices) {
            val map = HashMap<String, Any>()
            val result = results[i]

            map[from[0]] = "#" + parties[result.partyId]
            map[from[1]] = result.partyId
            map[from[2]] = result.votes
            map[from[3]] = getPercentageOfVotes(result.votes)
            map[from[4]] = result.elects!!

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

    private fun getPercentageOfVotes(partyVotes: Int): Float {
        val percentage = (partyVotes.toFloat() / election.validVotes.toFloat()) * 100
        return percentage.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toFloat()
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