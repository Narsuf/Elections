package com.jorgedguezm.elections.view.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.view.adapters.SectionsPagerAdapter

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
    }
}
