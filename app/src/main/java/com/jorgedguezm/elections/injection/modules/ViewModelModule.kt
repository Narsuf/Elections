package com.jorgedguezm.elections.injection.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jorgedguezm.elections.factory.AppViewModelFactory
import com.jorgedguezm.elections.ui.main.PlaceholderViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(PlaceholderViewModel::class)
    internal abstract fun bindHomeViewModel(galleryViewModel: PlaceholderViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(
            factory: AppViewModelFactory): ViewModelProvider.Factory
}