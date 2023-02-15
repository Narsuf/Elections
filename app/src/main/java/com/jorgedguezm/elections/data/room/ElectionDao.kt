package com.jorgedguezm.elections.data.room

import androidx.room.*
import com.jorgedguezm.elections.data.room.models.ElectionRaw
import com.jorgedguezm.elections.data.room.models.ElectionWithResultsAndParty
import com.jorgedguezm.elections.data.room.models.PartyRaw
import com.jorgedguezm.elections.data.room.models.ResultRaw

@Dao
interface ElectionDao {

    @Transaction
    @Query("SELECT * FROM elections")
    suspend fun getElections(): List<ElectionWithResultsAndParty>

    @Transaction
    @Query("SELECT * FROM elections WHERE id = :id")
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

            val result = it.result
            result.partyId = party.id
            result.electionId = election.id
            insertResult(result)
        }
    }

    suspend fun insertElectionsWithResultsAndParty(
        electionsWithResultsAndParty: List<ElectionWithResultsAndParty>
    ) {
        electionsWithResultsAndParty.forEach { insertElectionWithResultsAndParty(it) }
    }
}
