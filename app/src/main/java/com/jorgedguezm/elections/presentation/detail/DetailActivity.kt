package com.jorgedguezm.elections.presentation.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.Toolbar
import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.data.models.Election
import com.jorgedguezm.elections.databinding.ActivityDetailBinding
import com.jorgedguezm.elections.presentation.common.Constants
import com.jorgedguezm.elections.presentation.common.Constants.KEY_CONGRESS
import com.jorgedguezm.elections.presentation.common.Constants.KEY_CONGRESS_ELECTION
import com.jorgedguezm.elections.presentation.common.Constants.KEY_SENATE
import com.jorgedguezm.elections.presentation.common.Constants.KEY_SENATE_ELECTION
import com.jorgedguezm.elections.presentation.common.inheritance.ViewModelActivity

class DetailActivity : ViewModelActivity() {

    internal lateinit var binding: ActivityDetailBinding
    internal lateinit var currentElection: Election
    private lateinit var congressElection: Election
    private lateinit var senateElection: Election

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val extras = intent.extras
        congressElection = extras?.getSerializable(KEY_CONGRESS_ELECTION) as Election
        currentElection = congressElection
        senateElection = extras.getSerializable(KEY_SENATE_ELECTION) as Election

        binding.toolbar.setup()

        beginTransactionToDetailFragment()
    }

    private fun Toolbar.setup() {
        title = generateToolbarTitle()
        inflateMenu(R.menu.menu_detail_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_congress -> {
                if (currentElection.chamberName == KEY_SENATE) {
                    currentElection = congressElection
                    beginTransactionToDetailFragment()
                }
                true
            }

            R.id.action_senate -> {
                if (currentElection.chamberName == KEY_CONGRESS) {
                    currentElection = senateElection
                    beginTransactionToDetailFragment()
                }
                true
            }

            else -> false
        }
    }

    private fun beginTransactionToDetailFragment() {
        val detailFragment = DetailFragment()
        val transaction = supportFragmentManager.beginTransaction()

        detailFragment.arguments = Bundle().apply {
            putSerializable(Constants.KEY_ELECTION, currentElection)
        }

        transaction.replace(R.id.detail_frame, detailFragment)
        transaction.commit()
        binding.toolbar.title = generateToolbarTitle()
    }

    @VisibleForTesting
    internal fun generateToolbarTitle() = currentElection.chamberName + " (" + currentElection.date + ")"
}
