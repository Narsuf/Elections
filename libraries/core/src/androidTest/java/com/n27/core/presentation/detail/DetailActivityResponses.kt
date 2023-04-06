package com.n27.core.presentation.detail

import org.intellij.lang.annotations.Language

internal interface DetailActivityResponses {

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

        @Language("xml")
        val localElection = """
            <escrutinio_sitio>
                <num_a_elegir>9</num_a_elegir>
                <id>01_04_01</id>
                <nombre_lugar>Ayuntamiento</nombre_lugar>
                <nombre_disputado>concejales</nombre_disputado>
                <porciento_escrutado>100</porciento_escrutado>
                <nombre_sitio>Abla</nombre_sitio>
                <convocatoria>2019</convocatoria>
                <ts>1558964411</ts>
                <tipo_sitio>5</tipo_sitio>
                <votos>
                    <contabilizados>
                        <cantidad>817</cantidad>
                        <porcentaje>79.79</porcentaje>
                    </contabilizados>
                    <abstenciones>
                        <cantidad>207</cantidad>
                        <porcentaje>20.21</porcentaje>
                    </abstenciones>
                    <nulos>
                        <cantidad>8</cantidad>
                        <porcentaje>0.98</porcentaje>
                    </nulos>
                    <blancos>
                        <cantidad>4</cantidad>
                        <porcentaje>0.49</porcentaje>
                    </blancos>
                </votos>
                <resultados>
                    <numero_partidos>2</numero_partidos>
                    <partido>
                        <id_partido>1002824</id_partido>
                        <nombre>PP</nombre>
                        <electos>5</electos>
                        <votos_numero>454</votos_numero>
                        <votos_porciento>56.12</votos_porciento>
                    </partido>
                    <partido>
                        <id_partido>1014775</id_partido>
                        <nombre>PSOE-A</nombre>
                        <electos>4</electos>
                        <votos_numero>351</votos_numero>
                        <votos_porciento>43.39</votos_porciento>
                    </partido>
                </resultados>
            </escrutinio_sitio>
        """.trimIndent()
    }
}
