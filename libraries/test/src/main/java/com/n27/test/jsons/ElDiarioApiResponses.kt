package com.n27.test.jsons

import org.intellij.lang.annotations.Language

interface ElDiarioApiResponses {

    companion object {

        @Language("json")
        val congressElection = """
            {
              "1999": {
                "i": {
                  "abst": 23286,
                  "bl": 185,
                  "censo": 54300,
                  "escrutado": 10000,
                  "nl": 244,
                  "ok": 31014
                },
                "v": {
                  "0083": {
                    "v": 9104,
                    "s": 1,
                    "p": 2959
                  },
                  "0017": {
                    "v": 8925,
                    "s": 0,
                    "p": 2901
                  }
                }
              },
              "9999": {
                "i": {
                  "abst": 10506203,
                  "bl": 216515,
                  "censo": 34872054,
                  "escrutado": 10000,
                  "nl": 249499,
                  "ok": 24365851
                },
                "v": {
                  "0094": {
                    "v": 6752983,
                    "s": 120,
                    "p": 2800
                  },
                  "0083": {
                    "v": 5019869,
                    "s": 88,
                    "p": 2082
                  },
                  "0073": {
                    "v": 1159,
                    "s": 0,
                    "p": 0
                  }
                }
              },
              "0104": {
                "i": {
                  "abst": 157156,
                  "bl": 2681,
                  "censo": 460642,
                  "escrutado": 10000,
                  "nl": 2988,
                  "ok": 303486
                },
                "v": {
                  "0094": {
                    "v": 88425,
                    "s": 2,
                    "p": 2943
                  },
                  "0078": {
                    "v": 24089,
                    "s": 0,
                    "p": 802
                  }
                }
              }
            }
        """.trimIndent()

        @Language("json")
        val senateElection = """
            {
              "1999": {
                "i": {
                  "abst": 22967,
                  "bl": 326,
                  "censo": 54300,
                  "escrutado": 9870,
                  "nl": 765,
                  "ok": 30629
                },
                "v": {
                  "0083": {
                    "v": 0,
                    "s": 2,
                    "p": 0
                  },
                  "0017": {
                    "v": 0,
                    "s": 0,
                    "p": 0
                  }
                }
              },
              "9999": {
                "i": {
                  "abst": 10589496,
                  "bl": 449166,
                  "censo": 34872227,
                  "escrutado": 9974,
                  "nl": 559823,
                  "ok": 24190917
                },
                "v": {
                  "0094": {
                    "v": 0,
                    "s": 92,
                    "p": 0
                  },
                  "0018": {
                    "v": 0,
                    "s": 0,
                    "p": 0
                  }
                }
              },
              "0104": {
                "i": {
                  "abst": 157712,
                  "bl": 5529,
                  "censo": 460637,
                  "escrutado": 10000,
                  "nl": 8045,
                  "ok": 302925
                },
                "v": {
                  "0083": {
                    "v": 282862,
                    "s": 2,
                    "p": 9592
                  },
                  "0116": {
                    "v": 71875,
                    "s": 0,
                    "p": 2437
                  }
                }
              }
            }
        """.trimIndent()

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
