package com.jorgedguezm.elections.view.ui.main

import android.os.Bundle

import com.jorgedguezm.elections.R
import com.jorgedguezm.elections.compose.ViewModelActivity
import com.jorgedguezm.elections.databinding.ActivityMainBinding
import com.jorgedguezm.elections.view.adapters.SectionsPagerAdapter

class MainActivity : ViewModelActivity() {

    internal val binding by binding<ActivityMainBinding>(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        binding.viewPager.adapter = sectionsPagerAdapter
        binding.tabs.setupWithViewPager(binding.viewPager)
    }
}
