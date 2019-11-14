package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.KEY_CALLED_FROM
import com.jorgedguezm.elections.Constants.KEY_CONGRESS
import com.jorgedguezm.elections.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.Constants.KEY_ELECTION
import com.jorgedguezm.elections.Constants.KEY_GENERAL
import com.jorgedguezm.elections.Constants.KEY_SENATE
import com.jorgedguezm.elections.Constants.KEY_SENATE_ELECTION
import com.jorgedguezm.elections.data.Election

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*

import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    private val bundle = Bundle()

    private lateinit var currentElection: Election
    private lateinit var congressElection: Election
    private lateinit var senateElection: Election
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
        congressElection = extras.getSerializable(KEY_CONGRESS_ELECTION) as Election
        currentElection = congressElection
        senateElection = extras.getSerializable(KEY_SENATE_ELECTION) as Election

        toolbar.title = getToolbarTitle()

        beginTransaction()
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
                if (currentElection.chamberName == KEY_SENATE) {
                    currentElection = congressElection
                    beginTransaction()
                }

                return true
            }

            R.id.action_senate -> {
                if (currentElection.chamberName == KEY_CONGRESS) {
                    currentElection = senateElection
                    beginTransaction()
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun beginTransaction() {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        bundle.putSerializable(KEY_ELECTION, currentElection)
        detailFragment.arguments = bundle
        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
        toolbar.title = getToolbarTitle()
    }

    private fun getToolbarTitle(): String {
        return currentElection.chamberName + " (" + currentElection.date + ")"
    }

    override fun onDestroy() {
        detailActivityViewModel.disposeElements()
        super.onDestroy()
    }
}