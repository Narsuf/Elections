package com.n27.elections.injection

import com.n27.core.data.injection.CoreDataModule
import com.n27.core.presentation.injection.CorePresentationModule
import com.n27.core.presentation.injection.DetailComponent
import com.n27.elections.data.api.injection.AppNetModule
import com.n27.elections.presentation.main.MainActivity
import com.n27.regional_live.ui.injection.RegionalLiveComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppNetModule::class,
        AppModule::class,
        SubcomponentsModule::class,
        CoreDataModule::class,
        CorePresentationModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun detailComponent(): DetailComponent.Factory
    fun regionalLiveComponent(): RegionalLiveComponent.Factory
}
