package com.n27.regional_live

import org.intellij.lang.annotations.Language

internal interface RegionalActivityResponses {

    companion object {

        @Language("xml")
        val regionalElection = """
            <escrutinio_sitio>
                <num_a_elegir>67</num_a_elegir>
                <id>02</id>
                <nombre_lugar>Parlamento</nombre_lugar>
                <nombre_disputado>escaños</nombre_disputado>
                <porciento_escrutado>99.64</porciento_escrutado>
                <nombre_sitio>Aragón</nombre_sitio>
                <convocatoria>2019</convocatoria>
                <ts>1558925629</ts>
                <tipo_sitio>2</tipo_sitio>
                <votos>
                    <contabilizados>
                        <cantidad>666362</cantidad>
                        <porcentaje>68.08</porcentaje>
                    </contabilizados>
                    <abstenciones>
                        <cantidad>312442</cantidad>
                        <porcentaje>31.92</porcentaje>
                    </abstenciones>
                    <nulos>
                        <cantidad>4496</cantidad>
                        <porcentaje>0.67</porcentaje>
                    </nulos>
                    <blancos>
                        <cantidad>6525</cantidad>
                        <porcentaje>0.99</porcentaje>
                    </blancos>
                </votos>
                <resultados>
                    <numero_partidos>16</numero_partidos>
                    <partido>
                        <id_partido>85</id_partido>
                        <nombre>PSOE</nombre>
                        <electos>24</electos>
                        <votos_numero>203933</votos_numero>
                        <votos_porciento>30.81</votos_porciento>
                    </partido>
                    <partido>
                        <id_partido>78</id_partido>
                        <nombre>PP</nombre>
                        <electos>16</electos>
                        <votos_numero>138158</votos_numero>
                        <votos_porciento>20.87</votos_porciento>
                    </partido>
                </resultados>
            </escrutinio_sitio>
        """.trimIndent()
    }
}
