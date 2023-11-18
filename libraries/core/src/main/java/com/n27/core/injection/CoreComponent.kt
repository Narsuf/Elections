package com.n27.core.injection

import com.n27.core.presentation.detail.DetailActivity
import com.n27.core.presentation.detail.dialog.DetailDialog
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Subcomponent
interface CoreComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CoreComponent
    }

    fun inject(loginActivity: DetailActivity)
    fun inject(detailDialog: DetailDialog)
}
