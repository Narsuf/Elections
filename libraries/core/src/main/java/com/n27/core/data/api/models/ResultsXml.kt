package com.n27.core.data.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

data class ResultsXml(

    @field:Element(name = "numero_partidos")
    var numberOfParties: Int? = null,

    @field:ElementList(name = "partido", inline = true)
    var parties: List<PartyXml>? = null
)
