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
    lateinit var sharedPreferences : SharedPreferences
    lateinit var sharedPreferencesEditor: SharedPreferences.Editor
    val isFirstLaunch = "isFirstLaunch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_main)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        supportActionBar?.hide()

        managePreferences()

        Glide.with(this).load(R.drawable.loading).into(viewGif)
    }

    fun managePreferences() {
        sharedPreferences = getPreferences(Context.MODE_PRIVATE)
        sharedPreferencesEditor = getPreferences(Context.MODE_PRIVATE).edit()

        if (sharedPreferences.getBoolean(isFirstLaunch, true)) {
            callDialog()
            sharedPreferencesEditor.putBoolean(isFirstLaunch, false)
            sharedPreferencesEditor.apply()
        }

    }

    fun callDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setMessage("Content needs to be downloaded")
        dialog.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialogInterface, i -> dialogInterface.cancel() })
        dialog.setNegativeButton("Exit application",
                DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                    finish()
                })
        dialog.create().show()
    }
}