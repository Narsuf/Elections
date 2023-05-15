package com.n27.regional_live.regionals

import com.n27.regional_live.regionals.models.RegionalsContentState.WithData
import com.n27.test.generators.getElectionsXml
import com.n27.test.generators.getParties

fun getRegionalsContent() = WithData(getElectionsXml(), getParties())
