package com.n27.core.data.remote.api.models

import org.simpleframework.xml.Element

data class VotesInfoXml(

    @field:Element(name = "cantidad")
    var votes: Int? = null,

    @field:Element(name = "porcentaje")
    var percentage: Float? = null
)
