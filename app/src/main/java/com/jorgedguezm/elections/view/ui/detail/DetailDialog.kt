package com.jorgedguezm.elections.view.ui.detail

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.fragment.app.DialogFragment

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.utils.Constants.KEY_ELECTION
import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.utils.Utils

import kotlinx.android.synthetic.main.detail_fragment.*

class DetailDialog : DialogFragment() {

    private var census = 0

    private lateinit var election: Election

    lateinit var utils: Utils

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as DetailActivity
        val layoutInflater = activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val inflatedLayout = layoutInflater.inflate(R.layout.detail_dialog, root)
        val window = AlertDialog.Builder(activity)

        election = arguments?.getSerializable(KEY_ELECTION) as Election
        census = election.validVotes + election.abstentions

        val textViewTitle = inflatedLayout.findViewById(R.id.text_view_title) as TextView
        val concatenatedText = election.chamberName + " " + election.place + " " +
                election.date

        textViewTitle.text = concatenatedText

        setSimpleAdapter(inflatedLayout)

        window.setView(inflatedLayout)
        window.setPositiveButton(resources.getString(R.string.close), null)

        return window.create()
    }

    private fun setSimpleAdapter(inflatedLayout: View) {
        val from = arrayOf("text", "number", "percentage")
        val to = intArrayOf(R.id.text_view_text, R.id.text_view_number, R.id.text_view_percentage)
        val textData = resources.getStringArray(R.array.detail_dialog_list_view_text)
        val numberData = arrayOf("", election.totalElects, election.validVotes,
                election.abstentions, election.nullVotes, election.blankVotes)

        val percentageData = getPercentageData()

        val arrayList = ArrayList<Map<String, Any>>()

        for (i in textData.indices) {
            val map = HashMap<String, Any>()

            map[from[0]] = textData[i]
            map[from[1]] = numberData[i]

            if (i == 1)
                map[from[2]] = percentageData[i]
            else
                map[from[2]] = percentageData[i] + " %"

            arrayList.add(map)
        }

        val adapter = SimpleAdapter(activity, arrayList, R.layout.list_item_detail_dialog, from, to)

        val listView = inflatedLayout.findViewById(R.id.list_view_general_information) as ListView
        listView.adapter = adapter
    }

    private fun getPercentageData(): Array<String> {
        val percentageOfParticipation = utils
                .getPercentageWithTwoDecimals(election.validVotes, census)

        val percentageOfAbstentions = utils
                .getPercentageWithTwoDecimals(election.abstentions, census)

        val percentageOfNull = utils
                .getPercentageWithTwoDecimals(election.nullVotes, election.validVotes)

        val percentageOfBlank = utils
                .getPercentageWithTwoDecimals(election.blankVotes, election.validVotes)

        return arrayOf(election.scrutinized.toString(), "", percentageOfParticipation.toString(),
                percentageOfAbstentions.toString(), percentageOfNull.toString(),
                percentageOfBlank.toString())
    }
}