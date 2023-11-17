package com.n27.elections.injection

import com.n27.core.presentation.injection.DetailComponent
import com.n27.regional_live.presentation.injection.RegionalLiveComponent
import dagger.Module

@Module(
    subcomponents = [
        DetailComponent::class,
        RegionalLiveComponent::class
    ]
)
class SubcomponentsModule
