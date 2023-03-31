package com.n27.core.data.local.room

import androidx.room.*
import com.n27.core.data.local.room.models.ElectionRaw
import com.n27.core.data.local.room.models.ElectionWithResultsAndParty
import com.n27.core.data.local.room.models.PartyRaw
import com.n27.core.data.local.room.models.ResultRaw

@Dao
interface ElectionDao {

    @Transaction
    @Query("SELECT * FROM elections")
    suspend fun getElections(): List<ElectionWithResultsAndParty>

    @Transaction
    @Query("SELECT * FROM elections WHERE electionId = :id")
    suspend fun getElection(id: Long): ElectionWithResultsAndParty

    @Transaction
    @Query("SELECT * FROM parties")
    suspend fun getParties(): List<PartyRaw>

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

            val result = it.result.apply {
                resultPartyId = party.partyId
                resultElectionId = election.electionId
            }
            insertResult(result)
        }
    }
}
