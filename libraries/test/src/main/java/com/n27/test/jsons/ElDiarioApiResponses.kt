package com.n27.test.jsons

import org.intellij.lang.annotations.Language

interface ElDiarioApiResponses {

    companion object {

        @Language("json")
        val regionalElection = """
            {
              "9999": {
                "i": {
                  "abst": 299185,
                  "bl": 10609,
                  "censo": 979418,
                  "escrutado": 9820,
                  "nl": 8144,
                  "ok": 662691,
                  "seats": 67
                },
                "v": {
                  "2659": {
                    "v": 20310,
                    "p": 310,
                    "s": 1
                  }
                }
              }
            }
        """.trimIndent()

        @Language("json")
        val regionalParties = """
            
            {
              "2659": {
                "group": "2659",
                "sigla": "IU",
                "color": "#007057",
                "block": "l"
              }
            }
        """.trimIndent()

        @Language("json")
        val localElection = """
            {
              "04001": {
                "i": {
                  "abst": 215,
                  "bl": 7,
                  "censo": 1015,
                  "escrutado": 10000,
                  "nl": 19,
                  "ok": 800,
                  "seats": 9
                },
                "v": {
                  "0030": {
                    "v": 411,
                    "p": 5262,
                    "s": 5
                  }
                }
              }
            }
        """.trimIndent()

        @Language("json")
        val localParties = """
            {
              "0030": {
                "group": "0030",
                "sigla": "PSOE",
                "color": "#E02020",
                "block": "l"
              }
            }
        """.trimIndent()
    }
}
