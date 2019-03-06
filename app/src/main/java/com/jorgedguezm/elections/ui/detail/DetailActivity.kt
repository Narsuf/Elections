package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import android.graphics.Color
import android.os.CountDownTimer
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_RESULTS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.utils.Utils

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*

import java.math.RoundingMode

import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    lateinit var partiesColor: HashMap<String, String>
    lateinit var congressElection: Election
    lateinit var congressResults: ArrayList<Results>
    lateinit var senateElection: Election
    lateinit var senateResults: ArrayList<Results>

    lateinit var countDownTimer: CountDownTimer

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        AndroidInjection.inject(this)

        val bundle = intent.extras
        partiesColor = bundle?.getSerializable(KEY_PARTIES) as HashMap<String, String>
        congressElection = bundle.getSerializable(KEY_CONGRESS_ELECTIONS) as Election
        congressResults = bundle.getSerializable(KEY_CONGRESS_RESULTS) as ArrayList<Results>
        senateElection = bundle.getSerializable(KEY_SENATE_ELECTIONS) as Election
        senateResults = bundle.getSerializable(KEY_SENATE_RESULTS) as ArrayList<Results>

        utils.drawPieChart(pie_chart, utils.getElectsFromResults(congressResults),
                utils.getColorsFromResults(congressResults, partiesColor))

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

        for (i in congressResults.indices) {
            val map = HashMap<String, Any>()
            val result = congressResults[i]

            map[from[0]] = "#" + partiesColor[result.partyId]
            map[from[1]] = result.partyId
            map[from[2]] = result.votes
            map[from[3]] = getPercentageOfVotes(result.votes)
            map[from[4]] = result.elects!!

            arrayList.add(map)
        }

        val adapter = SimpleAdapter(this, arrayList, R.layout.list_item_detail_activity,
                from, to)

        adapter.viewBinder = PartyColorBinder()

        list_view.adapter = adapter

        list_view.setOnItemClickListener { _, _, position, _ ->
            pie_chart.highlightValue(position.toFloat(), 0)
            countDownTimer.start()
        }
    }

    private fun getPercentageOfVotes(partyVotes: Int): Float {
        val percentage = (partyVotes.toFloat() / congressElection.validVotes.toFloat()) * 100
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