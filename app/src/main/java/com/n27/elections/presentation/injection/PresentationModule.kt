package com.n27.elections.presentation.injection

import com.n27.core.data.local.json.RegionRepositoryImpl
import com.n27.core.data.remote.api.LiveRepositoryImpl
import com.n27.core.domain.LiveUseCase
import com.n27.elections.data.ElectionRepositoryImpl
import com.n27.elections.domain.ElectionUseCase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PresentationModule {

    @Provides
    @Singleton
    fun provideElectionUseCase(repository: ElectionRepositoryImpl) = ElectionUseCase(repository)

    @Provides
    @Singleton
    fun providesLiveUseCase(
        liveRepository: LiveRepositoryImpl,
        regionRepository: RegionRepositoryImpl
    ) = LiveUseCase(liveRepository, regionRepository)
}