package com.jorgedguezm.elections.injection.modules

import com.jorgedguezm.elections.ui.MainFragment
import com.jorgedguezm.elections.ui.SplashActivity

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity(): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
}