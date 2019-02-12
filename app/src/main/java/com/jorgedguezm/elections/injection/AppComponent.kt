package com.jorgedguezm.elections.injection

import android.app.Application

import com.jorgedguezm.elections.injection.modules.AppModule
import com.jorgedguezm.elections.injection.modules.BuildersModule

import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, BuildersModule::class,
    AppModule::class])
interface AppComponent {
    fun inject(app: Application)
}