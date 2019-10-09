package com.jorgedguezm.elections.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.ui.adapters.GeneralCardAdapter

import dagger.android.support.AndroidSupportInjection

import kotlinx.android.synthetic.main.fragment_main.*

import javax.inject.Inject

class PlaceholderFragment : Fragment() {

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter

    @Inject
    lateinit var pageViewModelFactory: PageViewModelFactory
    lateinit var pageViewModel: PageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidSupportInjection.inject(this)

        pageViewModel = ViewModelProviders.of(this, pageViewModelFactory)
                .get(PageViewModel::class.java).apply {
                    setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
                }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)

        if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) setHasOptionsMenu(true)

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerView.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)

            // specify an viewAdapter (see also next example)
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
                pageViewModel.loadGeneralElections()
                pageViewModel.electionsResult().observe(this@PlaceholderFragment,
                        Observer<List<Election>> { congressElections ->
                            val sortedList = congressElections
                                    .sortedWith(compareByDescending { it.year })

                            generalCardAdapter.congressElections = sortedList.toTypedArray()
                            generalCardAdapter.fragment = this@PlaceholderFragment
                            adapter = generalCardAdapter
                })
            }
        }
    }

    override fun onDestroy() {
        pageViewModel.disposeElements()
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