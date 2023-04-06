package com.n27.regional_live.regionals

import com.n27.regional_live.regionals.models.RegionalsState.Content
import com.n27.test.generators.getElectionsXml
import com.n27.test.generators.getPartiesRaw

fun getRegionalsContent() = Content(getElectionsXml(), getPartiesRaw())
