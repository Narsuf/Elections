package com.n27.elections.injection

import com.n27.core.injection.DetailComponent
import com.n27.core.presentation.injection.CorePresentationModule
import com.n27.elections.data.api.injection.NetModule
import com.n27.elections.presentation.injection.AppPresentationModule
import com.n27.elections.presentation.main.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetModule::class,
        AppModule::class,
        SubcomponentsModule::class,
        AppPresentationModule::class,
        CorePresentationModule::class
    ]
)
interface AppComponent {

    fun inject(activity: MainActivity)
    fun detailComponent(): DetailComponent.Factory
}
