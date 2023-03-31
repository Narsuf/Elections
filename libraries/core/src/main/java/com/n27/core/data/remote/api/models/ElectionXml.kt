package com.n27.core.data.remote.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "escrutinio_sitio")
data class ElectionXml(

    @field:Element(name = "num_a_elegir")
    var totalElects: Int = 0,

    @field:Element(name = "id", required = false)
    var id: String? = null,

    @field:Element(name = "nombre_lugar")
    var chamberName: String = "",

    @field:Element(name = "nombre_disputado")
    var electName: String? = null,

    @field:Element(name = "porciento_escrutado")
    var scrutinized: Float = 0f,

    @field:Element(name = "nombre_sitio")
    var place: String = "",

    @field:Element(name = "convocatoria")
    var year: String = "",

    @field:Element(name = "ts")
    var timestamp: Long? = null,

    @field:Element(name = "tipo_sitio")
    var type: Int? = null,

    @field:Element(name = "votos")
    var votes: VotesXml? = null,

    @field:Element(name = "resultados")
    var results: ResultsXml? = null
)
