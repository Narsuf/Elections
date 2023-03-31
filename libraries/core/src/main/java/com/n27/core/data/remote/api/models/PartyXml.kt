package com.n27.core.data.remote.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "partido")
data class PartyXml(

    @field:Element(name = "id_partido")
    var id: Long = 0,

    @field:Element(name = "nombre")
    var name: String = "",

    @field:Element(name = "electos")
    var elects: Int = 0,

    @field:Element(name = "votos_numero")
    var votes: Int = 0,

    @field:Element(name = "votos_porciento")
    var votePercentage: Float? = null,
)
