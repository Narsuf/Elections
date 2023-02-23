package org.n27.elections.presentation.detail

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

import org.n27.elections.R
import org.n27.elections.data.models.Election
import org.n27.elections.presentation.common.Constants.KEY_ELECTION
import org.n27.elections.presentation.common.PresentationUtils

import dagger.android.support.AndroidSupportInjection
import java.text.NumberFormat.getIntegerInstance

import javax.inject.Inject

class DetailDialog : DialogFragment() {

    private lateinit var election: Election

    @Inject
    lateinit var utils: PresentationUtils

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as DetailActivity
        val layoutInflater = activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val inflatedLayout = layoutInflater.inflate(R.layout.detail_dialog, null)
        val window = AlertDialog.Builder(activity)

        election = arguments?.getSerializable(KEY_ELECTION) as Election

        val textViewTitle = inflatedLayout.findViewById(R.id.text_view_title) as TextView
        val concatenatedText = election.chamberName + " " + election.place + " " + election.date

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
        val numberData = arrayOf(
            "",
            getIntegerInstance().format(election.totalElects),
            getIntegerInstance().format(election.validVotes),
            getIntegerInstance().format(election.abstentions),
            getIntegerInstance().format(election.nullVotes),
            getIntegerInstance().format(election.blankVotes)
        )
        val percentageData = election.getPercentageData()

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

    private fun Election.getPercentageData(): Array<String> {
        val census = validVotes + abstentions

        return with(utils) {
            val percentageOfParticipation = getPercentageWithTwoDecimals(validVotes, census)
            val percentageOfAbstentions = getPercentageWithTwoDecimals(abstentions, census)
            val percentageOfNull = getPercentageWithTwoDecimals(nullVotes, validVotes)
            val percentageOfBlank = getPercentageWithTwoDecimals(blankVotes, validVotes)

            arrayOf(
                scrutinized.toString(),
                "",
                percentageOfParticipation.toString(),
                percentageOfAbstentions.toString(),
                percentageOfNull.toString(),
                percentageOfBlank.toString()
            )
        }
    }
}
