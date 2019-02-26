package com.jorgedguezm.elections.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_main.*

import javax.inject.Inject

class MainFragment : Fragment() {

    lateinit var elections: List<Election>

    var viewAdapter = GeneralCardAdapter(ArrayList<Election>().toTypedArray())

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
                    elections = it
                    loadParties()
                })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }
    }

    override fun onDestroy() {
        electionsViewModel.disposeElements()
        super.onDestroy()
    }

    private fun loadParties() {
        electionsViewModel.loadParties()
        electionsViewModel.partiesResult().observe(this,
                Observer<List<Party>> {
                    for (p in it)
                        viewAdapter.parties[p.name] = p.color

                    when (arguments?.getInt(ARG_SECTION_NUMBER)) {
                        1 -> {
                            createGeneralCards()
                        }
                    }
                })
    }

    private fun createGeneralCards() {
        val generalElections = ArrayList<Election>()

        for (e in elections)
            if (e.name == "Generales") generalElections.add(e)

        viewAdapter.elections = generalElections
                .sortedWith(compareByDescending<Election> {it.year}.thenBy {it.chamberName})
                .toTypedArray()

        recyclerView.adapter = viewAdapter
    }
}