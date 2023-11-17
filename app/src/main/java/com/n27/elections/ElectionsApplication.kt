package com.n27.elections

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.n27.core.injection.CoreComponentProvider
import com.n27.elections.injection.AppComponent
import com.n27.elections.injection.AppModule
import com.n27.elections.injection.DaggerAppComponent
import com.n27.regional_live.injection.RegionalLiveComponentProvider

class ElectionsApplication : MultiDexApplication(), CoreComponentProvider, RegionalLiveComponentProvider {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()

    override fun onCreate() {
        FirebaseApp.initializeApp(this)
        super.onCreate()
    }

    override fun provideCoreComponent() = appComponent.coreComponent().create()
    override fun provideRegionalLiveComponent() = appComponent.regionalLiveComponent().create()
}
