package com.jorgedguezm.elections

import androidx.multidex.MultiDexApplication

import com.jorgedguezm.elections.injection.AppModule
import com.jorgedguezm.elections.injection.DaggerAppComponent
import com.jorgedguezm.elections.injection.NetModule

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector

import timber.log.Timber

import javax.inject.Inject

class ElectionsApplication: MultiDexApplication(), HasAndroidInjector {

    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule())
                .build().inject(this)

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}