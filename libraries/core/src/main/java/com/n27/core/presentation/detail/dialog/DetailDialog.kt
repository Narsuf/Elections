package com.n27.core.presentation.detail.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.fragment.app.DialogFragment
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.R
import com.n27.core.databinding.DialogDetailBinding
import com.n27.core.domain.election.models.Election
import com.n27.core.extensions.divide
import com.n27.core.presentation.PresentationUtils
import com.n27.core.presentation.detail.DetailActivity
import java.text.NumberFormat.getIntegerInstance
import javax.inject.Inject

class DetailDialog : DialogFragment() {

    private var _binding: DialogDetailBinding? = null
    private val binding get() = _binding!!
    @Inject internal lateinit var utils: PresentationUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailActivity).detailComponent.inject(this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogDetailBinding.inflate(layoutInflater)

        val election = arguments?.getSerializable(KEY_ELECTION) as Election

        val concatenatedText = "${election.chamberName} ${election.place} ${election.date}"
        binding.titleDialogDetail.text = concatenatedText
        binding.listDialogDetail.setSimpleAdapter(election)

        return AlertDialog.Builder(activity).apply {
            setView(binding.root)
            setPositiveButton(resources.getString(R.string.close), null)
        }.create()
    }

    private fun ListView.setSimpleAdapter(election: Election) = with(election) {
        val keys = arrayOf("text", "number", "percentage")
        val res = intArrayOf(
            R.id.text_list_item_dialog_detail,
            R.id.number_list_item_dialog_detail,
            R.id.percentage_list_item_dialog_detail
        )

        val textData = resources.getStringArray(R.array.text_array_dialog_detail)
        val numberData = arrayOf(
            getIntegerInstance().format(totalElects),
            getIntegerInstance().format(validVotes),
            getIntegerInstance().format(abstentions),
            getIntegerInstance().format(nullVotes),
            getIntegerInstance().format(blankVotes)
        )

        val percentageData = getPercentageData()

        val arrayList = ArrayList<Map<String, Any>>()

        for (i in textData.indices) {
            val map = HashMap<String, Any>()

            map[keys[0]] = textData[i]
            map[keys[1]] = numberData[i]

            if (i == 0)
                map[keys[2]] = percentageData[i]
            else
                map[keys[2]] = percentageData[i] + " %"

            arrayList.add(map)
        }

        adapter = SimpleAdapter(activity, arrayList, R.layout.list_item_dialog_detail, keys, res)
    }

    private fun Election.getPercentageData(): Array<String> {
        val census = validVotes + abstentions

        return utils.run {
            arrayOf(
                "",
                validVotes.divide(census),
                abstentions.divide(census),
                nullVotes.divide(validVotes),
                blankVotes.divide(validVotes)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
