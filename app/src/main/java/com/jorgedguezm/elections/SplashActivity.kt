package com.jorgedguezm.elections

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        // Set fullscreen UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // TODO Check if there is connection to the Internet
        if (true) {
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
            finish()
        } else {
            callErrorDialog()
        }
    }

    private fun callErrorDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle(resources.getString(R.string.no_internet_connection))
        dialog.setMessage(resources.getString(R.string.application_will_be_closed))
        dialog.setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
            finish()
        }
        dialog.create().show()
    }
}