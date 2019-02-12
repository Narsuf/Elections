package com.jorgedguezm.elections.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.snackbar.Snackbar

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.utils.Utils

import dagger.android.AndroidInjection

import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject lateinit var utils: Utils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        AndroidInjection.inject(this)

        // Set fullscreen UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        if (utils.isConnectedToInternet())
            callIntent()
        else
            noInternetConnection()
    }

    private fun callIntent() {
        val myIntent = Intent(this, MainActivity::class.java)
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
                callIntent()
            else
                handler.postDelayed(runnable, delay)
        }

        handler.post(runnable)
    }
}