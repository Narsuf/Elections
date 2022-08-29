package com.jorgedguezm.elections.data.injection.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.jorgedguezm.elections.presentation.main.MainViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindHomeViewModel(homeViewModel: MainViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}
