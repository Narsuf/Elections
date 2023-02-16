package com.narsuf.elections.presentation.injection

import com.narsuf.elections.presentation.detail.DetailActivity
import com.narsuf.elections.presentation.detail.DetailDialog
import com.narsuf.elections.presentation.detail.DetailFragment
import com.narsuf.elections.presentation.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ViewsModule {

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailActivity(): DetailActivity

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeDetailDialog(): DetailDialog
}
