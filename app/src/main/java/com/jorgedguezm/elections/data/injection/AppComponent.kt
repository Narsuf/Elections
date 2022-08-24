package com.jorgedguezm.elections.data.injection

import com.jorgedguezm.elections.ElectionsApplication
import com.jorgedguezm.elections.data.injection.vm.ViewModelModule

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class,
    AppModule::class,  BuildersModule::class, NetModule::class, ViewModelModule::class])
interface AppComponent {
    fun inject(app: ElectionsApplication)
}
