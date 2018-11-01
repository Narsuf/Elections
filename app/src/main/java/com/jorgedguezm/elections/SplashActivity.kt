package com.jorgedguezm.elections

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.splash_main.*

class SplashActivity : AppCompatActivity() {
    private val isFirstLaunch = "isFirstLaunch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        // Set fullscreen UI
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        if (getPreferences(Context.MODE_PRIVATE).getBoolean(isFirstLaunch, true))
            callDialog()

        // Load GIF
        Glide.with(this).load(R.drawable.loading).into(viewGif)
    }

    private fun callDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(resources.getString(R.string.content_needs_to_be_downloaded))
        dialog.setPositiveButton(resources.getString(R.string.download),
                { dialogInterface, _ ->
                    dialogInterface.cancel()
                    downloadContent()
                })
        dialog.setNegativeButton(resources.getString(R.string.not_now),
                { dialogInterface, _ ->
                    dialogInterface.cancel()
                    finish()
                })
        dialog.create().show()
    }

    private fun downloadContent() {
        val url = "http://90.171.38.91:8000/general_information/"

        val request = StringRequest(Request.Method.GET, url,
                { response ->
                    // TODO
                },
                { error -> Log.e("Request failed", error.message)})

        Volley.newRequestQueue(this).add(request)

        /*getPreferences(Context.MODE_PRIVATE)
                .edit().putBoolean(isFirstLaunch, false).apply()*/
    }
}