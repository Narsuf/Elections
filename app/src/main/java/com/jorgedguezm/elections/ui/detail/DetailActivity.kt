package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.utils.Utils

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*
import java.math.RoundingMode

import javax.inject.Inject
import android.R.id
import android.R.layout
import android.graphics.Color
import android.view.View
import android.widget.SimpleAdapter
import android.widget.TextView


class DetailActivity : AppCompatActivity() {

    lateinit var election: Election
    lateinit var partiesColor: HashMap<String, String>
    lateinit var results: ArrayList<Results>

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        AndroidInjection.inject(this)

        val bundle = intent.extras
        election = bundle?.getSerializable(KEY_ELECTIONS) as Election
        partiesColor = bundle.getSerializable(KEY_PARTIES) as HashMap<String, String>
        results = bundle.getSerializable(KEY_RESULTS) as ArrayList<Results>

        utils.drawPieChart(pie_chart, utils.getElectsFromResults(results),
                utils.getColorsFromResults(results, partiesColor))

        setSimpleAdapter()
    }

    private fun setSimpleAdapter() {
        val from = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val to = intArrayOf(R.id.tvPartyColor, R.id.tvPartyName, R.id.tvNumberVotes,
                R.id.tvVotesPercentage, R.id.tvElects)

        val arrayList = ArrayList<Map<String, Any>>()

        for (i in results.indices) {
            val map = HashMap<String, Any>()
            val result = results[i]

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
        val listOptions = list_view
        listOptions.adapter = adapter
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