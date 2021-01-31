package com.jorgedguezm.elections.view.ui.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.compose.ViewModelFragment
import com.jorgedguezm.elections.utils.Constants.KEY_ELECTION
import com.jorgedguezm.elections.models.entities.Election
import com.jorgedguezm.elections.utils.Utils
import com.jorgedguezm.elections.view.binders.PartyColorBinder

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.detail_fragment.*

import javax.inject.Inject

class DetailFragment : ViewModelFragment() {

    private val vm by viewModel<DetailViewModel>()

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
            activity?.supportFragmentManager?.let { dialog.show(it, "DetailDialog") }
        }

        utils.drawPieChart(pie_chart, election.results)

        initializeCountDownTimer()

        vm.adapter.observe(viewLifecycleOwner, Observer {
            it.viewBinder = PartyColorBinder()

            list_view.adapter = it

            list_view.setOnItemClickListener { _, _, position, _ ->
                pie_chart.highlightValue(position.toFloat(), 0)
                countDownTimer.start()
            }
        })

        vm.postElection(election)
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() { pie_chart.highlightValue(-1F, -1) }
        }
    }
}