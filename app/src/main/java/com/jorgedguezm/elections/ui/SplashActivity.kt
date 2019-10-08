package com.jorgedguezm.elections.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.google.android.material.snackbar.Snackbar

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Constants.Companion.KEY_ELECTIONS
import com.jorgedguezm.elections.Constants.Companion.KEY_PARTIES
import com.jorgedguezm.elections.data.Election
import com.jorgedguezm.elections.data.Party
import com.jorgedguezm.elections.Utils

import dagger.android.AndroidInjection

import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    private val electionsParams = Bundle()

    @Inject
    lateinit var utils: Utils

    @Inject
    lateinit var electionsViewModelFactory: ElectionsViewModelFactory
    lateinit var electionsViewModel: ElectionsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        AndroidInjection.inject(this)

        electionsViewModel = ViewModelProviders.of(this, electionsViewModelFactory).get(
                ElectionsViewModel::class.java)

        // Set fullscreen UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (utils.isConnectedToInternet())
            loadElections()
        else
            noInternetConnection()
    }

    private fun loadElections() {
        electionsViewModel.loadElections()
        electionsViewModel.electionsResult().observe(this,
                Observer<List<Election>> {
                    electionsParams.putSerializable(KEY_ELECTIONS, ArrayList(it))
                    loadParties()
                })
    }

    private fun loadParties() {
        electionsViewModel.loadParties()
        electionsViewModel.partiesResult().observe(this,
                Observer<List<Party>> {
                    electionsParams.putSerializable(KEY_PARTIES, ArrayList(it))
                    callIntent()
                })
    }

    private fun callIntent() {
        val myIntent = Intent(this, MainActivity::class.java)
        myIntent.putExtras(electionsParams)
        startActivity(myIntent)
        finish()
    }

    private fun noInternetConnection() {
        val handler = Handler()
        val delay = 1000L
        lateinit var runnable : Runnable

        Snackbar.make(findViewById(R.id.splash_main),
                resources.getString(R.string.no_internet_connection),
                Snackbar.LENGTH_LONG).show()

        runnable = Runnable {
            if (utils.isConnectedToInternet())
                loadElections()
            else
                handler.postDelayed(runnable, delay)
        }

        handler.post(runnable)
    }
}