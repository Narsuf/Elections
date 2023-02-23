package org.n27.elections.injection

import org.n27.elections.ElectionsApplication
import org.n27.elections.data.injection.NetModule
import org.n27.elections.presentation.injection.PresentationModule
import org.n27.elections.presentation.injection.ViewModelModule
import org.n27.elections.presentation.injection.ViewsModule
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
