package com.n27.core.injection

import com.n27.core.presentation.detail.DetailActivity
import com.n27.core.presentation.detail.DetailDialog
import com.n27.core.presentation.detail.DetailFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Subcomponent
interface DetailComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DetailComponent
    }

    fun inject(loginActivity: DetailActivity)
    fun inject(detailFragment: DetailFragment)
    fun inject(detailDialog: DetailDialog)
}
