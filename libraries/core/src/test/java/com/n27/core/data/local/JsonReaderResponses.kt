package com.n27.core.data.local

import org.intellij.lang.annotations.Language

internal interface JsonReaderResponses {

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
                      "votes": 7215530,
                      "election": 3
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
                }
              ]
            }
        """.trimIndent()
    }
}
