package com.jorgedguezm.elections.injection

import com.jorgedguezm.elections.ElectionsApplication

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