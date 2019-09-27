package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.Companion.KEY_CALLED_FROM
import com.jorgedguezm.elections.Constants.Companion.KEY_CONGRESS
import com.jorgedguezm.elections.Constants.Companion.KEY_CONGRESS_ELECTIONS
import com.jorgedguezm.elections.Constants.Companion.KEY_CONGRESS_RESULTS
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTION
import com.jorgedguezm.elections.Constants.Companion.KEY_GENERAL
import com.jorgedguezm.elections.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.Constants.Companion.KEY_SENATE
import com.jorgedguezm.elections.Constants.Companion.KEY_SENATE_ELECTIONS
import com.jorgedguezm.elections.Constants.Companion.KEY_SENATE_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results

import kotlinx.android.synthetic.main.detail_activity.*

class DetailActivity : AppCompatActivity() {

    private val bundle = Bundle()
    private var electionName = KEY_CONGRESS

    private lateinit var partiesColor: HashMap<String, String>
    private lateinit var congressElection: Election
    private lateinit var congressResults: ArrayList<Results>
    private lateinit var senateElection: Election
    private lateinit var senateResults: ArrayList<Results>

    private lateinit var calledFrom: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        val extras = intent.extras
        calledFrom = extras?.getSerializable(KEY_CALLED_FROM) as String
        partiesColor = extras.getSerializable(KEY_PARTIES) as HashMap<String, String>
        congressElection = extras.getSerializable(KEY_CONGRESS_ELECTIONS) as Election
        congressResults = extras.getSerializable(KEY_CONGRESS_RESULTS) as ArrayList<Results>
        senateElection = extras.getSerializable(KEY_SENATE_ELECTIONS) as Election
        senateResults = extras.getSerializable(KEY_SENATE_RESULTS) as ArrayList<Results>

        bundle.putSerializable(KEY_PARTIES, partiesColor)
        bundle.putSerializable(KEY_ELECTION, congressElection)
        bundle.putSerializable(KEY_RESULTS, congressResults)

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
        val id = item.itemId

        when (id) {
            R.id.action_congress -> {
                if (electionName == KEY_SENATE) {
                    bundle.putSerializable(KEY_ELECTION, congressElection)
                    bundle.putSerializable(KEY_RESULTS, congressResults)
                    beginTransaction()
                    electionName = KEY_CONGRESS
                }

                return true
            }

            R.id.action_senate -> {
                if (electionName == KEY_CONGRESS) {
                    bundle.putSerializable(KEY_ELECTION, senateElection)
                    bundle.putSerializable(KEY_RESULTS, senateResults)
                    beginTransaction()
                    electionName = KEY_SENATE
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun beginTransaction() {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        detailFragment.arguments = bundle
        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
    }
}