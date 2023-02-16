package com.narsuf.elections.injection

import com.narsuf.elections.ElectionsApplication
import com.narsuf.elections.data.injection.NetModule
import com.narsuf.elections.presentation.injection.PresentationModule
import com.narsuf.elections.presentation.injection.ViewModelModule
import com.narsuf.elections.presentation.injection.ViewsModule
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
