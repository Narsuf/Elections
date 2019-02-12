package com.jorgedguezm.elections

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment

import com.jorgedguezm.elections.injection.DaggerAppComponent
import com.jorgedguezm.elections.injection.modules.AppModule

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector

import javax.inject.Inject

class ElectionsApplication: Application(), HasActivityInjector, HasSupportFragmentInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>
    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build().inject(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}