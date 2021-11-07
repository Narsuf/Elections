package com.jorgedguezm.elections.injection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jorgedguezm.elections.view.ui.detail.DetailViewModel
import com.jorgedguezm.elections.view.ui.main.PlaceholderViewModel

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
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    internal abstract fun bindDetailViewModel(galleryViewModel: DetailViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(
            factory: ViewModelFactory
    ): ViewModelProvider.Factory
}