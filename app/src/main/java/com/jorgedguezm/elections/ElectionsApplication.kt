package com.jorgedguezm.elections

import android.app.Application
import androidx.fragment.app.Fragment

import com.jorgedguezm.elections.injection.DaggerAppComponent
import com.jorgedguezm.elections.injection.modules.AppModule

import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector

import javax.inject.Inject

class ElectionsApplication: Application(), HasSupportFragmentInjector {

    @Inject lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build().inject(this)
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector
}