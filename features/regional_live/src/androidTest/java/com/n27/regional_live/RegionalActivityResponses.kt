package com.n27.regional_live

import org.intellij.lang.annotations.Language

internal interface RegionalActivityResponses {

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
                  "1462": {
                    "v": 1116,
                    "p": 17,
                    "s": 0
                  },
                  "1469": {
                    "v": 843,
                    "p": 12,
                    "s": 0
                  },
                  "2658": {
                    "v": 1209,
                    "p": 18,
                    "s": 0
                  },
                  "2659": {
                    "v": 20310,
                    "p": 310,
                    "s": 1
                  },
                  "2660": {
                    "v": 33454,
                    "p": 511,
                    "s": 3
                  },
                  "2661": {
                    "v": 13746,
                    "p": 210,
                    "s": 1
                  },
                  "2662": {
                    "v": 26087,
                    "p": 398,
                    "s": 1
                  },
                  "2663": {
                    "v": 32717,
                    "p": 499,
                    "s": 3
                  },
                  "2664": {
                    "v": 538,
                    "p": 8,
                    "s": 0
                  },
                  "4929": {
                    "v": 8382,
                    "p": 128,
                    "s": 0
                  },
                  "0006": {
                    "v": 232712,
                    "p": 3555,
                    "s": 28
                  },
                  "0030": {
                    "v": 193170,
                    "p": 2951,
                    "s": 23
                  },
                  "0007": {
                    "v": 73677,
                    "p": 1125,
                    "s": 7
                  },
                  "0031": {
                    "v": 3208,
                    "p": 49,
                    "s": 0
                  },
                  "0270": {
                    "v": 2769,
                    "p": 42,
                    "s": 0
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
              "1462": {
                "group": "1462",
                "sigla": "EQUO",
                "color": "#D6D6D6",
                "block": "o"
              },
              "1469": {
                "group": "1469",
                "sigla": "PCTE",
                "color": "#b80000",
                "block": "o"
              },
              "2658": {
                "group": "2658",
                "sigla": "FIA",
                "color": "#D6D6D6",
                "block": "o"
              },
              "2659": {
                "group": "2659",
                "sigla": "IU",
                "color": "#007057",
                "block": "l"
              },
              "2660": {
                "group": "2660",
                "sigla": "CHA",
                "color": "#ad0017",
                "block": "l"
              },
              "2661": {
                "group": "2661",
                "sigla": "PAR",
                "color": "#fdbb03",
                "block": "o"
              },
              "2662": {
                "group": "2662",
                "sigla": "Podemos",
                "color": "#7C4080",
                "block": "l"
              },
              "2663": {
                "group": "2663",
                "sigla": "A. Existe",
                "color": "#610f13",
                "block": "o"
              },
              "2664": {
                "group": "2664",
                "sigla": "ETXSBC",
                "color": "#D6D6D6",
                "block": "o"
              },
              "2665": {
                "group": "4929",
                "sigla": "Cs",
                "color": "#EA8046",
                "block": "r"
              },
              "4926": {
                "group": "2658",
                "sigla": "FIA",
                "color": "#D6D6D6",
                "block": "o"
              },
              "4927": {
                "group": "2662",
                "sigla": "Podemos",
                "color": "#7C4080",
                "block": "l"
              },
              "4928": {
                "group": "2663",
                "sigla": "A. Existe",
                "color": "#610f13",
                "block": "o"
              },
              "4929": {
                "group": "4929",
                "sigla": "Cs",
                "color": "#EA8046",
                "block": "r"
              },
              "5418": {
                "group": "2658",
                "sigla": "FIA",
                "color": "#D6D6D6",
                "block": "o"
              },
              "5419": {
                "group": "4929",
                "sigla": "Cs",
                "color": "#EA8046",
                "block": "r"
              },
              "0006": {
                "group": "0006",
                "sigla": "PP",
                "color": "#02A2DD",
                "block": "r"
              },
              "0007": {
                "group": "0007",
                "sigla": "Vox",
                "color": "#AAD656",
                "block": "r"
              },
              "0030": {
                "group": "0030",
                "sigla": "PSOE",
                "color": "#E02020",
                "block": "l"
              },
              "0031": {
                "group": "0031",
                "sigla": "PACMA",
                "color": "#B0BD21",
                "block": "o"
              },
              "0270": {
                "group": "0270",
                "sigla": "EB",
                "color": "#D6D6D6",
                "block": "o"
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
                  },
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
              "0030": {
                "group": "0030",
                "sigla": "PSOE",
                "color": "#E02020",
                "block": "l"
              },
              "0060": {
                "group": "0369",
                "sigla": "Podemos-IU",
                "color": "#7C4080",
                "block": "l"
              }
            }
        """.trimIndent()
    }
}
