package com.n27.core.presentation.detail

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import androidx.fragment.app.Fragment
import com.n27.core.Constants.KEY_ELECTION
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.databinding.FragmentDetailBinding
import com.n27.core.extensions.drawWithResults
import com.n27.core.presentation.common.PresentationUtils
import com.n27.core.presentation.detail.binders.PartyColorBinder
import java.text.NumberFormat.getIntegerInstance
import javax.inject.Inject

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    internal val binding get() = _binding!!

    @Inject internal lateinit var utils: PresentationUtils
    internal lateinit var countDownTimer: CountDownTimer
    private lateinit var election: Election

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as DetailActivity).detailComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        election = arguments?.getSerializable(KEY_ELECTION) as Election

        binding.setupViews()
        return binding.root
    }

    private fun FragmentDetailBinding.setupViews() {
        floatingButtonMoreInfo.setOnClickListener {
            val dialog = DetailDialog().also { it.arguments = arguments }
            activity?.supportFragmentManager?.let { dialog.show(it, "DetailDialog") }

            utils.track("results_info_clicked") {
                param("election", "${election.chamberName} (${election.date})")
            }
        }

        // Prepare chart
        pieChart.drawWithResults(election.results)
        initializeCountDownTimer()

        // Fill ListView with election data.
        val resultsAdapter = election.generateResultsAdapter()
        resultsAdapter.viewBinder = PartyColorBinder()

        listView.adapter = resultsAdapter
        listView.setOnItemClickListener { _, _, position, _ ->
            pieChart.highlightValue(position.toFloat(), 0)
            countDownTimer.start()

            utils.track("party_clicked") { param("party", election.results[position].party.name) }
        }
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() { binding.pieChart.highlightValue(-1F, -1) }
        }
    }

    private fun Election.generateResultsAdapter(): SimpleAdapter {
        val keys = arrayOf("color", "partyName", "numberVotes", "votesPercentage", "elects")
        val resources = intArrayOf(
            R.id.tvPartyColor,
            R.id.tvPartyName,
            R.id.tvNumberVotes,
            R.id.tvVotesPercentage,
            R.id.tvElects
        )

        val arrayList = ArrayList<Map<String, Any>>()

        for (r in results) {
            val map = HashMap<String, Any>()

            map[keys[0]] = "#" + r.party.color
            map[keys[1]] = r.party.name
            map[keys[2]] = getIntegerInstance().format(r.votes)
            map[keys[3]] = if (chamberName == KEY_SENATE)
                "- %"
            else
                utils.getPercentageWithTwoDecimals(r.votes, validVotes) + " %"

            map[keys[4]] = r.elects

            arrayList.add(map)
        }

        return SimpleAdapter(context, arrayList, R.layout.list_item_detail_activity, keys, resources)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
        _binding = null
    }
}
