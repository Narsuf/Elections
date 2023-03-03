package com.n27.elections.injection

import com.n27.core.injection.DetailComponent
import dagger.Module

@Module(
    subcomponents = [
        DetailComponent::class
    ]
)
class SubcomponentsModule
