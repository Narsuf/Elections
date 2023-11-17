package com.n27.core.injection

import com.n27.core.data.injection.CoreDataModule
import com.n27.core.presentation.detail.DetailActivity
import com.n27.core.presentation.detail.dialog.DetailDialog
import com.n27.core.presentation.injection.CorePresentationModule
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
    fun inject(detailDialog: DetailDialog)
}
