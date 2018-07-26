package com.jorgedguezm.elections

import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.View

import com.bumptech.glide.Glide

import kotlinx.android.synthetic.main.splash_main.*

class SplashActivity : AppCompatActivity() {
    val isFirstLaunch = "isFirstLaunch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        if (getPreferences(Context.MODE_PRIVATE).getBoolean(isFirstLaunch, true)) {
            callDialog()
        }

        Glide.with(this).load(R.drawable.loading).into(viewGif)
    }

    private fun callDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage(resources.getString(R.string.content_needs_to_be_downloaded))
        dialog.setPositiveButton(resources.getString(R.string.download),
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                    downloadContent()
                })
        dialog.setNegativeButton(resources.getString(R.string.not_now),
                DialogInterface.OnClickListener { dialogInterface, _ ->
                    dialogInterface.cancel()
                    finish()
                })
        dialog.create().show()
    }

    private fun downloadContent() {
        getPreferences(Context.MODE_PRIVATE)
                .edit().putBoolean(isFirstLaunch, false).apply()
    }
}