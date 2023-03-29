package com.n27.regional_live.ui.regional_live

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n27.regional_live.R
import com.n27.regional_live.databinding.ActivityRegionalLiveBinding
import com.n27.regional_live.ui.injection.RegionalLiveComponent
import com.n27.regional_live.ui.injection.RegionalLiveComponentProvider

class RegionalLiveActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegionalLiveBinding
    internal lateinit var regionalLiveComponent: RegionalLiveComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        regionalLiveComponent = (applicationContext as RegionalLiveComponentProvider).provideRegionalLiveComponent()
        regionalLiveComponent.inject(this)
        super.onCreate(savedInstanceState)

        binding = ActivityRegionalLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarActivityRegionalLive)

        val navView: BottomNavigationView = binding.navViewActivityRegionalLive

        val navController = findNavController(R.id.nav_host_fragment_activity_regional_live)
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_regionals, R.id.navigation_locals))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
