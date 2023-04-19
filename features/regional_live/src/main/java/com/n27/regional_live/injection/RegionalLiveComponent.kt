package com.n27.regional_live.injection

import com.n27.regional_live.RegionalLiveActivity
import com.n27.regional_live.locals.LocalsFragment
import com.n27.regional_live.locals.dialog.MunicipalitySelectionDialog
import com.n27.regional_live.regionals.RegionalsFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope

@ActivityScope
@Subcomponent
interface RegionalLiveComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): RegionalLiveComponent
    }

    fun inject(regionalLiveActivity: RegionalLiveActivity)
    fun inject(regionalsFragment: RegionalsFragment)
    fun inject(localsFragment: LocalsFragment)
    fun inject(municipalitySelectionDialog: MunicipalitySelectionDialog)
}
