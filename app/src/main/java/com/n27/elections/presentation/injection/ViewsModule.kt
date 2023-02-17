package com.n27.elections.presentation.injection

import com.n27.elections.presentation.detail.DetailActivity
import com.n27.elections.presentation.detail.DetailDialog
import com.n27.elections.presentation.detail.DetailFragment
import com.n27.elections.presentation.main.MainActivity
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
