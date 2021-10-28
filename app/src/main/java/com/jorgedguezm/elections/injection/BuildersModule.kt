package com.jorgedguezm.elections.injection

import com.jorgedguezm.elections.view.ui.detail.DetailActivity
import com.jorgedguezm.elections.view.ui.detail.DetailFragment
import com.jorgedguezm.elections.view.ui.main.MainActivity
import com.jorgedguezm.elections.view.ui.main.PlaceholderFragment

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): PlaceholderFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity
}