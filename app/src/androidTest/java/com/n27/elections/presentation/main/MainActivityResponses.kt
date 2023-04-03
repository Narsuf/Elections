package com.n27.elections.presentation.main

import org.intellij.lang.annotations.Language

internal interface MainActivityResponses {

    companion object {

        @Language("json")
        val elections = """
            {
              "elections": [
                {
                  "id": 3,
                  "results": [
                    {
                      "id": 17,
                      "partyId": 1,
                      "electionId": 3,
                      "party": {
                        "id": 1,
                        "name": "PP",
                        "color": "006EC7"
                      },
                      "elects": 123,
                      "votes": 7215530
                    }
                  ],
                  "date": "2015",
                  "name": "Generales",
                  "place": "Espa\u00f1a",
                  "chamberName": "Congreso",
                  "totalElects": 350,
                  "scrutinized": 100.0,
                  "validVotes": 25349824,
                  "abstentions": 9280429,
                  "blankVotes": 187766,
                  "nullVotes": 226994
                },
                {
                  "id": 4,
                  "results": [
                    {
                      "id": 18,
                      "partyId": 1,
                      "electionId": 4,
                      "party": {
                        "id": 1,
                        "name": "PP",
                        "color": "006EC7"
                      },
                      "elects": 101,
                      "votes": 0
                    }
                  ],
                  "date": "2015",
                  "name": "Generales",
                  "place": "Espa\u00f1a",
                  "chamberName": "Senado",
                  "totalElects": 208,
                  "scrutinized": 99.99,
                  "validVotes": 25752839,
                  "abstentions": 8120062,
                  "blankVotes": 519409,
                  "nullVotes": 580989
                }
              ]
            }
        """.trimIndent()
    }
}
