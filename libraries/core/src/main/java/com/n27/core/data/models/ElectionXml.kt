package com.n27.core.data.models

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList

data class ElectionXml(

    @field:Element(name = "num_a_elegir")
    val totalElects: Int,

    @field:Element(name = "id")
    val id: Int,

    @field:Element(name = "nombre_lugar")
    val chamberName: String,

    @field:Element(name = "nombre_disputado")
    val electName: String,

    @field:Element(name = "porciento_escrutado")
    val scrutinized: Float,

    @field:Element(name = "nombre_sitio")
    val place: String,

    @field:Element(name = "convocatoria")
    val year: Int,

    @field:Element(name = "ts")
    val timeStamp: Long,

    @field:Element(name = "tipo_sitio")
    val type: Int,

    @field:Element(name = "votos")
    val votes: VoteType,

    @field:Element(name = "resultados")
    val results: List<Results>
) {

    @Element(name = "votos")
    data class VoteType(

        @field:Element(name = "contabilizados")
        val valid: Votes,

        @field:Element(name = "abstenciones")
        val abstention: Votes,

        @field:Element(name = "nulos")
        val nulls: Votes,

        @field:Element(name = "blancos")
        val blank: Votes
    ) {

        data class Votes(

            @field:Element(name = "cantidad")
            val number: Int,

            @field:Element(name = "porcentaje")
            val percentage: Float
        )
    }

    data class Results(

        @field:Element(name = "numero_partidos")
        val partiesNumber: Int,

        @field:ElementList(name = "partido")
        val partidos: List<Party>
    ) {

        data class Party(

            @field:Element(name = "id_partido")
            val id: Int,

            @field:Element(name = "nombre")
            val name: String,

            @field:Element(name = "electos")
            val elects: Int,

            @field:Element(name = "votos_numero")
            val votes: Int,

            @field:Element(name = "votos_porciento")
            val percentage: Float
        )
    }
}
