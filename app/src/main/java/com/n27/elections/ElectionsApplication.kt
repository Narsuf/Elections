package com.n27.elections

import androidx.multidex.MultiDexApplication
import com.google.firebase.FirebaseApp
import com.n27.core.BuildConfig
import com.n27.core.injection.DetailComponentProvider
import com.n27.elections.injection.AppComponent
import com.n27.elections.injection.AppModule
import com.n27.elections.injection.DaggerAppComponent
import timber.log.Timber

class ElectionsApplication : MultiDexApplication(), DetailComponentProvider {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .appModule(AppModule(this))
        .build()

    override fun onCreate() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        FirebaseApp.initializeApp(this)

        super.onCreate()
    }

    override fun provideDetailComponent() = appComponent.detailComponent().create()
}
