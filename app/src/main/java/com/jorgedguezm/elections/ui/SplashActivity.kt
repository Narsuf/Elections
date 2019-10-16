package com.jorgedguezm.elections.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.Utils

import dagger.android.AndroidInjection

import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

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

        callIntent()
    }

    private fun callIntent() {
        val myIntent = Intent(this, MainActivity::class.java)
        startActivity(myIntent)
        finish()
    }
}