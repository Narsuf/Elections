package com.jorgedguezm.elections.presentation.main

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.databinding.ActivityMainBinding
import com.jorgedguezm.elections.presentation.common.inheritance.ViewModelActivity
import com.jorgedguezm.elections.presentation.main.adapters.GeneralCardAdapter
import javax.inject.Inject

class MainActivity : ViewModelActivity() {

    internal lateinit var binding: ActivityMainBinding
    private val vm by viewModel<MainViewModel>()

    @Inject
    lateinit var generalCardAdapter: GeneralCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.recyclerView.adapter = generalCardAdapter
        binding.recyclerView.apply {
            // use a linear layout manager
            layoutManager = LinearLayoutManager(context)

            vm.electionsResult.observe(this@MainActivity) { state ->
                when (state) {
                    MainViewState.Loading -> Unit

                    is MainViewState.Error -> {
                        Snackbar.make(
                            this,
                            context.getString(R.string.something_wrong),
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                    is MainViewState.Success -> {
                        val generalCardAdapter = adapter as GeneralCardAdapter
                        generalCardAdapter.elections = state.elections
                        adapter = generalCardAdapter
                    }
                }
            }

            if (vm.electionsResult.value == null) vm.loadElections("España")
        }
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return if (item.itemId == R.id.action_show_historical)
            true
        else
            super.onOptionsItemSelected(item)
    }*/
}
