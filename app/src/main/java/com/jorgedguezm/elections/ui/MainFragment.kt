package com.jorgedguezm.elections.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.data.Election

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_main.view.*

import javax.inject.Inject

class MainFragment : Fragment() {

    @Inject
    lateinit var electionsViewModelFactory: ElectionsViewModelFactory
    lateinit var electionsViewModel: ElectionsViewModel

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        fun newInstance(sectionNumber: Int): MainFragment {
            val fragment = MainFragment()
            val args = Bundle()
            args.putInt(ARG_SECTION_NUMBER, sectionNumber)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        electionsViewModel = ViewModelProviders.of(this, electionsViewModelFactory).get(
                ElectionsViewModel::class.java)

        electionsViewModel.loadElections()
        electionsViewModel.electionsResult().observe(this,
                Observer<List<Election>> {
                    Log.d("Elections", it.toString())
                })

        electionsViewModel.electionsError().observe(this,
                Observer<String> {
                    Log.d("Error", it)
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_main, container, false)
        rootView.section_label.text = getString(R.string.section_format,
                arguments?.getInt(ARG_SECTION_NUMBER))
        return rootView
    }
}