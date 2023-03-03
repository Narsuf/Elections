package com.n27.elections.presentation.injection

import com.n27.core.presentation.common.PresentationUtils
import com.n27.elections.presentation.main.adapters.GeneralCardAdapter
import dagger.Module
import dagger.Provides

@Module
class AppPresentationModule {

    @Provides
    fun provideGeneralCardAdapter(utils: PresentationUtils) = GeneralCardAdapter(utils)
}
