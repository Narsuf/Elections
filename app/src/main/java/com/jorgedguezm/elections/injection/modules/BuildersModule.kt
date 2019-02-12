package com.jorgedguezm.elections.injection.modules

import com.jorgedguezm.elections.ui.MainFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment
}