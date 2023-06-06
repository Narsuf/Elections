package com.n27.regional_live.presentation.locals

import com.n27.regional_live.presentation.locals.models.LocalsState.Content
import com.n27.test.generators.getRegions

fun getLocalsContent() = Content(getRegions().regions)