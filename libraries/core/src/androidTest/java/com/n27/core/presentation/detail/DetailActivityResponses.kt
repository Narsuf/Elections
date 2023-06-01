package com.n27.core.presentation.detail

import org.intellij.lang.annotations.Language

internal interface DetailActivityResponses {

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
              },
              "9922": {
                "i": {
                  "abst": 51903,
                  "bl": 2398,
                  "censo": 165270,
                  "escrutado": 9947,
                  "nl": 1691,
                  "ok": 112493,
                  "seats": 18
                },
                "v": {
                  "1462": {
                    "v": 1116,
                    "p": 100,
                    "s": 0
                  },
                  "2658": {
                    "v": 362,
                    "p": 32,
                    "s": 0
                  },
                  "2659": {
                    "v": 2834,
                    "p": 255,
                    "s": 0
                  },
                  "2660": {
                    "v": 6075,
                    "p": 548,
                    "s": 1
                  },
                  "2661": {
                    "v": 3643,
                    "p": 328,
                    "s": 0
                  },
                  "2662": {
                    "v": 4326,
                    "p": 390,
                    "s": 0
                  },
                  "2663": {
                    "v": 3524,
                    "p": 318,
                    "s": 0
                  },
                  "2664": {
                    "v": 538,
                    "p": 48,
                    "s": 0
                  },
                  "2665": {
                    "v": 1700,
                    "p": 153,
                    "s": 0
                  },
                  "0006": {
                    "v": 39161,
                    "p": 3534,
                    "s": 8
                  },
                  "0030": {
                    "v": 33306,
                    "p": 3005,
                    "s": 7
                  },
                  "0007": {
                    "v": 11244,
                    "p": 1014,
                    "s": 2
                  },
                  "0270": {
                    "v": 575,
                    "p": 51,
                    "s": 0
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
                  "0006": {
                    "v": 347,
                    "p": 4443,
                    "s": 4
                  },
                  "0263": {
                    "v": 16,
                    "p": 204,
                    "s": 0
                  }
                }
              },
              "04002": {
                "i": {
                  "abst": 221,
                  "bl": 14,
                  "censo": 1053,
                  "escrutado": 10000,
                  "nl": 14,
                  "ok": 832,
                  "seats": 9
                },
                "v": {
                  "0030": {
                    "v": 516,
                    "p": 6308,
                    "s": 6
                  },
                  "0006": {
                    "v": 239,
                    "p": 2921,
                    "s": 3
                  },
                  "0007": {
                    "v": 49,
                    "p": 599,
                    "s": 0
                  }
                }
              }
            }
        """.trimIndent()

        @Language("json")
        val localParties = """
            {
              "0006": {
                "group": "0006",
                "sigla": "PP",
                "color": "#02A2DD",
                "block": "R"
              }
            }
        """.trimIndent()
    }
}
