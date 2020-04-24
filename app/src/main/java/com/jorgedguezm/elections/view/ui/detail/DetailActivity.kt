package com.jorgedguezm.elections.view.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.utils.Constants.KEY_CONGRESS
import com.jorgedguezm.elections.utils.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.utils.Constants.KEY_SENATE
import com.jorgedguezm.elections.utils.Constants.KEY_SENATE_ELECTION
import com.jorgedguezm.elections.utils.Utils
import com.jorgedguezm.elections.models.entities.Election

import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.detail_activity.*

import javax.inject.Inject

class DetailActivity : AppCompatActivity() {

    internal lateinit var currentElection: Election
    private lateinit var congressElection: Election
    private lateinit var senateElection: Election

    @Inject
    lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_activity)

        AndroidInjection.inject(this)

        setSupportActionBar(toolbar)

        val extras = intent.extras
        congressElection = extras?.getSerializable(KEY_CONGRESS_ELECTION) as Election
        currentElection = congressElection
        senateElection = extras.getSerializable(KEY_SENATE_ELECTION) as Election

        toolbar.title = utils.generateToolbarTitle(currentElection)

        utils.beginTransactionToDetailFragment(this, currentElection)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_detail_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_congress -> {
                if (currentElection.chamberName == KEY_SENATE) {
                    currentElection = congressElection
                    utils.beginTransactionToDetailFragment(this, currentElection)
                }

                return true
            }

            R.id.action_senate -> {
                if (currentElection.chamberName == KEY_CONGRESS) {
                    currentElection = senateElection
                    utils.beginTransactionToDetailFragment(this, currentElection)
                }

                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }
}