package com.n27.core.presentation.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import com.n27.core.Constants
import com.n27.core.Constants.KEY_CONGRESS
import com.n27.core.Constants.KEY_CONGRESS_ELECTION
import com.n27.core.Constants.KEY_SENATE
import com.n27.core.Constants.KEY_SENATE_ELECTION
import com.n27.core.R
import com.n27.core.data.models.Election
import com.n27.core.databinding.ActivityDetailBinding
import com.n27.core.presentation.injection.DetailComponent
import com.n27.core.presentation.injection.DetailComponentProvider

class DetailActivity : AppCompatActivity() {

    internal lateinit var binding: ActivityDetailBinding
    internal lateinit var currentElection: Election
    internal lateinit var detailComponent: DetailComponent
    private lateinit var congressElection: Election
    private lateinit var senateElection: Election

    override fun onCreate(savedInstanceState: Bundle?) {
        detailComponent = (applicationContext as DetailComponentProvider).provideDetailComponent()
        detailComponent.inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val extras = intent.extras
        congressElection = extras?.getSerializable(KEY_CONGRESS_ELECTION) as Election
        currentElection = congressElection
        senateElection = extras.getSerializable(KEY_SENATE_ELECTION) as Election

        title = generateToolbarTitle()

        beginTransactionToDetailFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_detail_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_swap -> {
                if (currentElection.chamberName == KEY_SENATE) {
                    currentElection = congressElection
                    beginTransactionToDetailFragment()
                } else if (currentElection.chamberName == KEY_CONGRESS) {
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
