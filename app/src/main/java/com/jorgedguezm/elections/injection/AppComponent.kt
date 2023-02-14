package com.jorgedguezm.elections.injection

import com.jorgedguezm.elections.ElectionsApplication
import com.jorgedguezm.elections.data.injection.NetModule
import com.jorgedguezm.elections.presentation.injection.PresentationModule
import com.jorgedguezm.elections.presentation.injection.ViewModelModule
import com.jorgedguezm.elections.presentation.injection.ViewsModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        ViewsModule::class,
        NetModule::class,
        ViewModelModule::class,
        PresentationModule::class
    ]
)
interface AppComponent {
    fun inject(app: ElectionsApplication)
}
