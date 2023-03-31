package com.n27.core.data.api.models

import org.simpleframework.xml.Element

data class VotesXml(

    @field:Element(name = "contabilizados")
    var valid: VotesInfoXml? = null,

    @field:Element(name = "abstenciones")
    var abstentions: VotesInfoXml? = null,

    @field:Element(name = "nulos")
    var notValid: VotesInfoXml? = null,

    @field:Element(name = "blancos")
    var blank: VotesInfoXml? = null
)
