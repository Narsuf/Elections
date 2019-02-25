package com.jorgedguezm.elections.injection

import com.jorgedguezm.elections.ElectionsApplication
import com.jorgedguezm.elections.injection.modules.AppModule
import com.jorgedguezm.elections.injection.modules.BuildersModule
import com.jorgedguezm.elections.injection.modules.NetModule

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AndroidSupportInjectionModule::class,
    BuildersModule::class, AppModule::class, NetModule::class])
interface AppComponent {
    fun inject(app: ElectionsApplication)
}