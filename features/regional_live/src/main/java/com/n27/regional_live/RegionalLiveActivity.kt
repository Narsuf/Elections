package com.n27.regional_live

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n27.regional_live.databinding.ActivityRegionalLiveBinding
import com.n27.regional_live.injection.RegionalLiveComponent
import com.n27.regional_live.injection.RegionalLiveComponentProvider

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
        binding.setUpNavigation()
    }

    private fun ActivityRegionalLiveBinding.setUpNavigation() {
        val navView: BottomNavigationView = navViewActivityRegionalLive
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_regional_live) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.navigation_regionals, R.id.navigation_locals))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}
