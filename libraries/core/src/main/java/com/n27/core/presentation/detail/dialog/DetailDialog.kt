package com.n27.core.presentation.detail.dialog

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
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import java.text.NumberFormat.getIntegerInstance
import javax.inject.Inject

class DetailDialog : DialogFragment() {

    private lateinit var election: Election
    @Inject internal lateinit var utils: PresentationUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailActivity).detailComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity as DetailActivity
        val layoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val inflatedLayout = layoutInflater.inflate(R.layout.dialog_detail, null)
        val window = AlertDialog.Builder(activity)

        election = arguments?.getSerializable(KEY_ELECTION) as Election

        val textViewTitle = inflatedLayout.findViewById(R.id.title_dialog_detail) as TextView
        val concatenatedText = "${election.chamberName} ${election.place} ${election.date}"
        textViewTitle.text = concatenatedText

        setSimpleAdapter(inflatedLayout)

        return window.apply {
            setView(inflatedLayout)
            setPositiveButton(resources.getString(R.string.close), null)
        }.create()
    }

    private fun setSimpleAdapter(inflatedLayout: View) {
        val keys = arrayOf("text", "number", "percentage")
        val res = intArrayOf(
            R.id.text_list_item_dialog_detail,
            R.id.number_list_item_dialog_detail,
            R.id.percentage_list_item_dialog_detail
        )

        val textData = resources.getStringArray(R.array.text_array_dialog_detail)
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

            map[keys[0]] = textData[i]
            map[keys[1]] = numberData[i]

            if (i == 1)
                map[keys[2]] = percentageData[i]
            else
                map[keys[2]] = percentageData[i] + " %"

            arrayList.add(map)
        }

        val adapter = SimpleAdapter(activity, arrayList, R.layout.list_item_dialog_detail, keys, res)

        val listView = inflatedLayout.findViewById(R.id.list_dialog_detail) as ListView
        listView.adapter = adapter
    }

    private fun Election.getPercentageData(): Array<String> {
        val census = validVotes + abstentions

        return utils.run {
            arrayOf(
                scrutinized.toString(),
                "",
                getPercentageWithTwoDecimals(validVotes, census),
                getPercentageWithTwoDecimals(abstentions, census),
                getPercentageWithTwoDecimals(nullVotes, validVotes),
                getPercentageWithTwoDecimals(blankVotes, validVotes)
            )
        }
    }
}
