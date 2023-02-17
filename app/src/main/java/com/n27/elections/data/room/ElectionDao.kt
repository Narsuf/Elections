package com.n27.elections.data.room

import androidx.room.*

@Dao
interface ElectionDao {

    @Transaction
    @Query("SELECT * FROM elections")
    suspend fun getElections(): List<ElectionWithResultsAndParty>

    @Transaction
    @Query("SELECT * FROM elections WHERE electionId = :id")
    suspend fun getElection(id: Long): ElectionWithResultsAndParty

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElection(election: ElectionRaw)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParty(election: PartyRaw)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(election: ResultRaw)

    suspend fun insertElectionWithResultsAndParty(
        electionWithResultsAndParty: ElectionWithResultsAndParty
    ) {
        val election = electionWithResultsAndParty.election
        val results = electionWithResultsAndParty.results

        insertElection(electionWithResultsAndParty.election)

        results.forEach {
            val party = it.party
            insertParty(party)

            val result = it.result.copy(
                resultPartyId = party.partyId,
                resultElectionId = election.electionId
            )
            insertResult(result)
        }
    }

    suspend fun insertElectionsWithResultsAndParty(
        electionsWithResultsAndParty: List<ElectionWithResultsAndParty>
    ) {
        electionsWithResultsAndParty.forEach { insertElectionWithResultsAndParty(it) }
    }
}
