package com.jorgedguezm.elections

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.multidex.MultiDexApplication

import com.jorgedguezm.elections.injection.DaggerAppComponent
import com.jorgedguezm.elections.injection.modules.AppModule
import com.jorgedguezm.elections.injection.modules.NetModule

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector

import javax.inject.Inject

class ElectionsApplication: MultiDexApplication(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .netModule(NetModule(BuildConfig.URL))
                .build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}