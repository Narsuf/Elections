package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.Companion.KEY_CALLED_FROM
import com.jorgedguezm.elections.Constants.Companion.KEY_CONGRESS
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTION
import com.jorgedguezm.elections.Constants.Companion.KEY_GENERAL
import com.jorgedguezm.elections.Constants.Companion.KEY_SENATE
import com.jorgedguezm.elections.data.Election

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*

import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    private val bundle = Bundle()
    private var electionName = KEY_CONGRESS

    private lateinit var election: Election
    private lateinit var calledFrom: String

    @Inject
    lateinit var detailActivityViewModelFactory: DetailActivityViewModelFactory
    lateinit var detailActivityViewModel: DetailActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        AndroidInjection.inject(this)

        detailActivityViewModel = ViewModelProviders.of(this,
                detailActivityViewModelFactory).get(DetailActivityViewModel::class.java)

        val extras = intent.extras
        calledFrom = extras?.getSerializable(KEY_CALLED_FROM) as String
        election = extras.getSerializable(KEY_ELECTION) as Election

        beginTransaction(election)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (calledFrom == KEY_GENERAL) {
            menuInflater.inflate(R.menu.menu_detail_activity, menu)
            return true
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_congress -> {
                if (electionName == KEY_SENATE) {
                    beginTransaction(election)
                    electionName = KEY_CONGRESS
                }

                return true
            }

            R.id.action_senate -> {
                if (electionName == KEY_CONGRESS) {
                    detailActivityViewModel.loadElection(election.year, election.place,
                            election.chamberName)
                    detailActivityViewModel.electionResult().observe(this,
                            Observer<Election> {
                                beginTransaction(it)
                                electionName = KEY_SENATE
                    })
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun beginTransaction(election: Election) {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        bundle.putSerializable(KEY_ELECTION, election)
        detailFragment.arguments = bundle
        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
    }

    override fun onDestroy() {
        detailActivityViewModel.disposeElements()
        super.onDestroy()
    }
}