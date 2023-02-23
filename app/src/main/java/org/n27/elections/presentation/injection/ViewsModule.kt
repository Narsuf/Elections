package org.n27.elections.presentation.injection

import org.n27.elections.presentation.detail.DetailActivity
import org.n27.elections.presentation.detail.DetailDialog
import org.n27.elections.presentation.detail.DetailFragment
import org.n27.elections.presentation.main.MainActivity
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
