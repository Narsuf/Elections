package com.n27.test

import androidx.multidex.MultiDexApplication
import com.n27.core.injection.DetailComponent
import com.n27.core.injection.DetailComponentProvider
import com.n27.core.presentation.common.PresentationUtils
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

class TestApplication : MultiDexApplication(), DetailComponentProvider {

    override fun provideDetailComponent(): TestApplicationComponent = DaggerTestApplicationComponent.create()
}

@Singleton
@Component(modules = [FakeModule::class])
interface TestApplicationComponent : DetailComponent

@Module
class FakeModule {

    @Provides
    @Singleton
    fun providePresentationUtils(): PresentationUtils = PresentationUtils()
}
