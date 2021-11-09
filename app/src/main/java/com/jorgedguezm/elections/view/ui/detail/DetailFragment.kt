package com.jorgedguezm.elections.view.ui.detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.databinding.FragmentDetailBinding
import com.jorgedguezm.elections.models.Election
import com.jorgedguezm.elections.utils.Constants.KEY_ELECTION
import com.jorgedguezm.elections.view.binders.PartyColorBinder
import com.jorgedguezm.elections.view.ui.ViewModelFragment

class DetailFragment : ViewModelFragment() {

    private val vm by viewModel<DetailViewModel>()

    internal lateinit var binding: FragmentDetailBinding
    internal lateinit var countDownTimer: CountDownTimer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = binding(inflater, R.layout.fragment_detail, container)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val election = arguments?.getSerializable(KEY_ELECTION) as Election

        binding.floatingButtonMoreInfo.setOnClickListener {
            val bundle = Bundle()
            val dialog = DetailDialog()

            bundle.putSerializable(KEY_ELECTION, election)
            dialog.arguments = bundle
            activity?.supportFragmentManager?.let { dialog.show(it, "DetailDialog") }
        }

        utils.drawPieChart(binding.pieChart, election.results)

        initializeCountDownTimer()

        vm.adapter.observe(viewLifecycleOwner, {
            it.viewBinder = PartyColorBinder()

            binding.listView.adapter = it
            binding.listView.setOnItemClickListener { _, _, position, _ ->
                binding.pieChart.highlightValue(position.toFloat(), 0)
                countDownTimer.start()
            }
        })

        vm.postElection(election)
    }

    private fun initializeCountDownTimer() {
        countDownTimer = object: CountDownTimer(1000, 1) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() { binding.pieChart.highlightValue(-1F, -1) }
        }
    }
}