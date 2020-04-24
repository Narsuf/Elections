package com.jorgedguezm.elections.view.ui.main

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.compose.ViewModelFragment
import com.jorgedguezm.elections.view.adapters.GeneralCardAdapter

import kotlinx.android.synthetic.main.fragment_main.*

import javax.inject.Inject

class PlaceholderFragment : ViewModelFragment() {

    internal val vm by viewModel<PlaceholderViewModel>()

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm.setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        vm.electionsResult.observe(viewLifecycleOwner, Observer { result ->
            result.data?.let { elections ->
                val sortedElections = elections.sortedWith(compareByDescending { it.date })

                generalCardAdapter.congressElections = sortedElections
                        .filter { it.chamberName == "Congreso" }

                generalCardAdapter.senateElections = sortedElections
                        .filter { it.chamberName == "Senado" }

                generalCardAdapter.fragment = this@PlaceholderFragment
                generalCardAdapter.notifyDataSetChanged()
            }
        })

        recyclerView.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)

            // specify an viewAdapter (see also next example)
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
                setHasOptionsMenu(true)

                recyclerView.adapter = generalCardAdapter

                if (generalCardAdapter.congressElections.isEmpty()) {
                    val nullString: String? = null
                    vm.postElection(Pair("España", nullString))
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_show_historical) return true

        return super.onOptionsItemSelected(item)
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
                arguments = Bundle().apply { putInt(ARG_SECTION_NUMBER, sectionNumber) }
            }
        }
    }
}