package com.jorgedguezm.elections.view.ui.main

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.material.snackbar.Snackbar

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.compose.ViewModelFragment
import com.jorgedguezm.elections.databinding.FragmentMainBinding
import com.jorgedguezm.elections.view.adapters.GeneralCardAdapter

import timber.log.Timber

import javax.inject.Inject

class PlaceholderFragment : ViewModelFragment() {

    private val vm by viewModel<PlaceholderViewModel>()
    private lateinit var binding: FragmentMainBinding

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = binding(inflater, R.layout.fragment_main, container)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.recyclerView.adapter = generalCardAdapter
        binding.recyclerView.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)

            // specify an viewAdapter (see also next example)
            if (arguments?.getInt(ARG_SECTION_NUMBER) == 1) {
                setHasOptionsMenu(true)

                vm.electionsResult.observe(viewLifecycleOwner, { state ->
                    when (state) {
                        MainViewState.Loading -> Unit
                        is MainViewState.Error -> {
                            Snackbar.make(this, context.getString(R.string.something_wrong),
                                Snackbar.LENGTH_LONG).show()
                        }

                        is MainViewState.Success -> {
                            val elections = state.elections.map { it.copy() }

                            elections.sortedByDescending {
                                if (it.date.length > 4)
                                    it.date.toDouble() / 10
                                else
                                    it.date.toDouble()
                            }.let { sortedElections ->
                                sortedElections.forEach {
                                    if (it.date.length > 4) {
                                        it.date = when (it.date) {
                                            "20192" -> "2019-10N"
                                            "20191" -> "2019-28A"
                                            else -> it.date
                                        }
                                    }
                                }

                                val generalCardAdapter = adapter as GeneralCardAdapter
                                generalCardAdapter.congressElections =
                                    sortedElections.filter { it.chamberName == "Congreso" }
                                generalCardAdapter.senateElections =
                                    sortedElections.filter { it.chamberName == "Senado" }
                                adapter = generalCardAdapter
                            }
                        }
                    }
                })

                vm.loadElections("España")
            }
        }

        return binding.root
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