package com.n27.test.jsons

import org.intellij.lang.annotations.Language

interface RegionalResponses {

    companion object {

        @Language("json")
        val regions = """
            {
              "regions": [
                {
                  "id": "01",
                  "name": "Andalucía"
                },
                {
                  "id": "19",
                  "name": "Melilla"
                }
              ]
            }
        """.trimIndent()

        @Language("json")
        val provinces = """
            {
              "regions": [
                {
                  "Andalucía": [
                    {
                      "id": "04",
                      "name": "Almería"
                    },
                    {
                      "id": "11",
                      "name": "Cádiz"
                    }
                  ]
                },
                {
                  "Melilla": [
                    {
                      "id": "52",
                      "name": "Melilla"
                    }
                  ]
                }
              ]
            }

        """.trimIndent()

        @Language("json")
        val municipalities = """
            {
              "provinces": [
                {
                  "Almería": [
                    {
                      "id": "01",
                      "name": "Abla"
                    },
                    {
                      "id": "02",
                      "name": "Abrucena"
                    }
                  ]
                },
                {
                  "Cádiz": [
                    {
                      "id": "01",
                      "name": "Alcalá de los Gazules"
                    },
                    {
                      "id": "02",
                      "name": "Alcalá del Valle"
                    }
                  ]
                },
                {
                  "Melilla": [
                    {
                      "id": "01",
                      "name": "Melilla"
                    }
                  ]
                }
              ]
            }

        """.trimIndent()
    }
}