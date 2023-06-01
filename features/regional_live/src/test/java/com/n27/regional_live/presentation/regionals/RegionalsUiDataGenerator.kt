package com.n27.regional_live.presentation.regionals

import com.n27.regional_live.presentation.regionals.models.RegionalsContentState.WithData
import com.n27.test.generators.getLiveElections

fun getRegionalsContent() = WithData(getLiveElections())
