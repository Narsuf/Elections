package com.jorgedguezm.elections.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_CONGRESS_RESULTS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_ELECTION
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_RESULTS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_ELECTIONS
import com.jorgedguezm.elections.constants.Constants.Companion.KEY_SENATE_RESULTS
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Results

import kotlinx.android.synthetic.main.detail_activity.*

class DetailActivity : AppCompatActivity() {

    var electionName = KEY_CONGRESS

    lateinit var partiesColor: HashMap<String, String>
    lateinit var congressElection: Election
    lateinit var congressResults: ArrayList<Results>
    lateinit var senateElection: Election
    lateinit var senateResults: ArrayList<Results>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        setSupportActionBar(toolbar)

        val extras = intent.extras
        partiesColor = extras?.getSerializable(KEY_PARTIES) as HashMap<String, String>
        congressElection = extras.getSerializable(KEY_CONGRESS_ELECTIONS) as Election
        congressResults = extras.getSerializable(KEY_CONGRESS_RESULTS) as ArrayList<Results>
        senateElection = extras.getSerializable(KEY_SENATE_ELECTIONS) as Election
        senateResults = extras.getSerializable(KEY_SENATE_RESULTS) as ArrayList<Results>

        val bundle = Bundle()
        bundle.putSerializable(KEY_PARTIES, partiesColor)

        if (electionName == KEY_CONGRESS) {
            bundle.putSerializable(KEY_ELECTION, congressElection)
            bundle.putSerializable(KEY_RESULTS, congressResults)
            beginTransaction(bundle)
        }
    }

    private fun beginTransaction(bundle: Bundle) {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        detailFragment.arguments = bundle
        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
    }
}