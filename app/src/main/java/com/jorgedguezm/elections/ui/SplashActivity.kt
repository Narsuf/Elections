package com.jorgedguezm.elections.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity

import com.google.android.material.snackbar.Snackbar

import com.jorgedguezm.elections.R

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        // Set fullscreen UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // TODO Check if there is connection to the Internet
        if (false)
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
            // TODO Check if there is connection to the Internet
            if (false)
                callIntent()

            handler.postDelayed(runnable, delay)
        }

        handler.post(runnable)
    }
}