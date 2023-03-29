package com.n27.core.data.api.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
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
    var votes: Votes? = null,

    @field:Element(name = "resultados")
    var results: Results? = null
) {

    data class Votes(

        @field:Element(name = "contabilizados")
        var valid: VotesInfo? = null,

        @field:Element(name = "abstenciones")
        var abstentions: VotesInfo? = null,

        @field:Element(name = "nulos")
        var notValid: VotesInfo? = null,

        @field:Element(name = "blancos")
        var blank: VotesInfo? = null
    ) {

        data class VotesInfo(

            @field:Element(name = "cantidad")
            var votes: Int? = null,

            @field:Element(name = "porcentaje")
            var percentage: Float? = null
        )
    }

    data class Results(

        @field:Element(name = "numero_partidos")
        var numberOfParties: Int? = null,

        @field:ElementList(name = "partido", inline = true)
        var parties: List<Party>? = null
    ) {

        @Root(name = "partido")
        data class Party(

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
    }
}
