package com.jorgedguezm.elections.ui.main

import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTIONS
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTIONS_BUNDLE
import com.jorgedguezm.elections.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.data.Results
import com.jorgedguezm.elections.ui.ElectionsViewModel
import com.jorgedguezm.elections.ui.ElectionsViewModelFactory
import com.jorgedguezm.elections.ui.adapters.GeneralCardAdapter

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_main.*

import javax.inject.Inject

class PlaceholderFragment : Fragment() {

    lateinit var parties: ArrayList<Party>
    lateinit var elections: ArrayList<Election>

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter

    @Inject
    lateinit var electionsViewModelFactory: ElectionsViewModelFactory
    lateinit var electionsViewModel: ElectionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        electionsViewModel = ViewModelProviders.of(this, electionsViewModelFactory).get(
                ElectionsViewModel::class.java)

        val electionsBundle = arguments!!.getBundle(KEY_ELECTIONS_BUNDLE)
        parties = electionsBundle?.getSerializable(KEY_PARTIES) as ArrayList<Party>
        elections = electionsBundle.getSerializable(KEY_ELECTIONS) as ArrayList<Election>

        when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            1 -> {
                setHasOptionsMenu(true)
                generalCardAdapter.fragment = this
                createGeneralElectionsCards()
            }
        }
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
            adapter = generalCardAdapter
        }
    }

    override fun onDestroy() {
        electionsViewModel.disposeElements()
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_show_historical) return true

        return super.onOptionsItemSelected(item)
    }

    private fun createGeneralElectionsCards() {
        val congressElections = ArrayList<Election>()
        val senateElections = ArrayList<Election>()

        for (p in parties)
            generalCardAdapter.partiesColor[p.name] = p.color

        for (e in elections) {
            if (e.name == "Generales" && e.chamberName == "Congreso")
                congressElections.add(e)
            else
                senateElections.add(e)
        }

        val sortedList = congressElections.sortedWith(compareByDescending {it.year})

        // These calls need to be made synchronously
        val handler = Handler()
        var index = 0
        lateinit var runnable: Runnable

        runnable = Runnable {
            electionsViewModel.loadResults(sortedList[index].year, sortedList[index].place,
                    sortedList[index].chamberName!!)

            electionsViewModel.resultsResult().observe(this,
                    Observer<List<Results>> {
                        generalCardAdapter.congressResults.add(it)
                        index++

                        if (index < sortedList.size) {
                            handler.post(runnable)
                        } else {
                            generalCardAdapter.congressElections = sortedList.toTypedArray()
                            generalCardAdapter.senateElections = senateElections.toTypedArray()
                            generalCardAdapter.electionsViewModel = electionsViewModel
                            recyclerView.adapter = generalCardAdapter
                        }
                    })
        }

        handler.post(runnable)
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }
}