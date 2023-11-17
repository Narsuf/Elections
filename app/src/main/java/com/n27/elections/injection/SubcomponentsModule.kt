package com.n27.elections.injection

import com.n27.core.injection.CoreComponent
import com.n27.regional_live.injection.RegionalLiveComponent
import dagger.Module

@Module(
    subcomponents = [
        CoreComponent::class,
        RegionalLiveComponent::class
    ]
)
class SubcomponentsModule
