package com.n27.regional_live.regionals

import com.n27.regional_live.presentation.regionals.models.RegionalsContentState.WithData
import com.n27.test.generators.getLiveElections
import com.n27.test.generators.getParties

fun getRegionalsContent() = WithData(getLiveElections(), getParties())
