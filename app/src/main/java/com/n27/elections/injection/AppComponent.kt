package com.n27.elections.injection

import com.n27.core.data.injection.CoreDataModule
import com.n27.core.injection.DetailComponent
import com.n27.core.presentation.injection.CorePresentationModule
import com.n27.elections.presentation.MainActivity
import com.n27.regional_live.presentation.injection.RegionalLiveComponent
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
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
