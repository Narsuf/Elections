package com.jorgedguezm.elections.injection.modules

import com.jorgedguezm.elections.ui.detail.DetailActivity
import com.jorgedguezm.elections.ui.detail.DetailFragment
import com.jorgedguezm.elections.ui.main.PlaceholderFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): PlaceholderFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}