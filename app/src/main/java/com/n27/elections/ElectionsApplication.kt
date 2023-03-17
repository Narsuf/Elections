package com.n27.elections

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.n27.core.BuildConfig
import com.n27.core.data.api.CoreNetModule
import com.n27.core.presentation.injection.DetailComponentProvider
import com.n27.elections.injection.AppComponent
import com.n27.elections.injection.AppModule
import com.n27.elections.injection.DaggerAppComponent
import com.n27.regional_live.ui.injection.RegionalLiveComponent
import com.n27.regional_live.ui.injection.RegionalLiveComponentProvider
import timber.log.Timber

class ElectionsApplication : MultiDexApplication(), DetailComponentProvider, RegionalLiveComponentProvider {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()

    override fun onCreate() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        FirebaseApp.initializeApp(this)

        super.onCreate()
    }

    override fun provideDetailComponent() = appComponent.detailComponent().create()
    override fun provideRegionalLiveComponent() = appComponent.regionalLiveComponent().create()
}
