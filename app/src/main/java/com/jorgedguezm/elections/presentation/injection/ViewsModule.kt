package com.jorgedguezm.elections.presentation.injection

import com.jorgedguezm.elections.presentation.detail.DetailActivity
import com.jorgedguezm.elections.presentation.detail.DetailDialog
import com.jorgedguezm.elections.presentation.detail.DetailFragment
import com.jorgedguezm.elections.presentation.main.MainActivity
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
